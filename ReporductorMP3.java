import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;


@SuppressWarnings("serial")
public class ReporductorMP3 extends JFrame implements ActionListener{
	static Thread hiloReproductor=null;	//hilo en el que se ejecutará el player
	static boolean suena=false;	//bandera que utilizo para saber cuando esta sonando la cancion
	
	static Player player;	//el reproductor
	
	static JButton play;
	static JButton stop;
	static JButton pause;
	static JTextField tiempo;	//mostrará el tiempo en segundos
	
	public static void main(String[] args) {
		new ReporductorMP3();
	}
	
	/*
	 * creo la interfaz grafica
	 */
	public ReporductorMP3(){
		setTitle("Reproductor de mp3 minimalista");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300,100);
		setLayout(new GridLayout(2,2));
		
		// creo los botones
		
		play=new JButton("PLAY");
		play.addActionListener(this);
		add(play);
		
		stop=new JButton("STOP");
		stop.addActionListener(this);
		add(stop);
		
		pause=new JButton("PAUSE");
		pause.addActionListener(this);
		add(pause);
		
		tiempo=new JTextField("0");
		tiempo.setEditable(false);
		//alineacion del texto del campo de texto hacia la derecha
		tiempo.setHorizontalAlignment(JTextField.RIGHT);
		add(tiempo);
		
		
		setVisible(true);
		//coloco la ventana en el centro de la pantalla
		setLocationRelativeTo(null);
	}

	/*
	 * Aqui se ejecuta el reproductor de musica ensi
	 */
	public static void  playmp3Thread(){
		new Thread(
				new Runnable() {
					public void run() {	
						// guardo el hilo en un objeto estatico para acceder desde fuera de la funcion
						hiloReproductor=Thread.currentThread();
						
						//creamos el dialogo predefinido de un selector de ficheros
						JFileChooser selectorFichero=new JFileChooser();

						// seleccion de la cancion que empezará a sonar
						// el selector se mostrá hasta que indiquemos un fichero
						int respuesta;
						do{
							respuesta =selectorFichero.showOpenDialog(play);
						}while(respuesta!=JFileChooser.APPROVE_OPTION);
						
						InputStream fis = null;
						try {
								// creamos el canal de entrada con el fichero que antes se indico
								fis = new FileInputStream(selectorFichero.getSelectedFile());
								// creo el reproductor
								player =new Player(fis);
								// indico que la cancion esta sonando
									// esto me biene bien para hacer despues una cosa u
									// en el listener de los botones
								suena=true; 
								// inicio un hilo que se encarga de mostrar en que segundo
								// se encuntra sonando la cancion
								songPosition();
								// hago que empiece a sonar la musica
								player.play();
								suena=false;
						} catch (JavaLayerException e) {
							System.out.println("nada mp3 con "+selectorFichero.getSelectedFile() );
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
		).start(); //inicia el hilo
	}
	
	/*
	 * Muestra en segundos la posicion de la cancion dentro del campo de texto tiempo
	 */
	public static void  songPosition(){
		new Thread(
				new Runnable() {
					public void run() {	
						//mientras que la cancion no finalice y suene(no esta en pausa)
						while(!player.isComplete() && suena){
							// consigo el tiempo en mls de la cancion lo paso a segundos
							// y lo meto en el campo de texto
							tiempo.setText( (player.getPosition()/1000)+" seg.");
						}	
					}
				}
		).start();
	}

	
	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource().equals(play)){		/*------------------------------------boton play--*/
			// si el hilo no esta iniciado lo inicio
				// esto evita que si se pulsa repetidamente el boton play
				// la cancion se reproduzca varias veces simultaneamente
			if(hiloReproductor==null){
				playmp3Thread();
			}else if(player.isComplete()) {
				playmp3Thread();
			}
		}else if(ae.getSource().equals(stop)){	/*------------------------------------boton stop--*/
			// si el hilod esta iniciado lo quito
			if(hiloReproductor!=null){
				hiloReproductor.stop();
				suena=false;
			}
		}else if(ae.getSource().equals(pause)){	/*-----------------------------------boton pause--*/
			// si esta sonando hago que el hilo se pare dejando
			// la cancion por el sengundo donde se estaba escuchando
			if(suena){
				hiloReproductor.suspend();
				suena=false;
				//System.out.println("hilo suspendido - "+hiloReproductor.getState());
			// si no miro si el hilo no es nulo, entonces continuo con la cancion por donde iva
			}else if(hiloReproductor!=null){
				suena=true;
				hiloReproductor.resume();
				//System.out.println("hilo resumido - "+hiloReproductor.getState());
			}
		}
	}	
}
