import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.SwingConstants;

public class Puzzle extends JFrame implements ActionListener {

    private JPanel contentPane;
    private JButton[] botones;
    private final String[] numeros = {" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};
    private JLabel lblTiempo;
    private Timer timer;
    private int segundos = 0;
    private int minutos = 0;
    private JButton btnPausarReanudar;
    private boolean pausado = false;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Puzzle frame = new Puzzle();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Puzzle() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 893, 667);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(204, 153, 0));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        botones();
        iniciarJuego();

        JButton botonReiniciar = new JButton("Reiniciar");
        botonReiniciar.addActionListener(this);
        botonReiniciar.setBounds(0, 582, 417, 48);
        contentPane.add(botonReiniciar);

        btnPausarReanudar = new JButton("Pausar");
        btnPausarReanudar.addActionListener(this);
        btnPausarReanudar.setBounds(418, 582, 461, 48);
        contentPane.add(btnPausarReanudar);

        lblTiempo = new JLabel("Tiempo: 00:00");
        lblTiempo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTiempo.setFont(new Font("Tahoma", Font.ITALIC, 20));
        lblTiempo.setBounds(41, 10, 811, 23);
        contentPane.add(lblTiempo);

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!pausado) {
                    segundos++;
                    if (segundos == 60) {
                        minutos++;
                        segundos = 0;
                    }
                    actualizarTiempo();
                }
            }
        });
        timer.start();
    }

    private void botones() {
        botones = new JButton[numeros.length];
        for (int i = 0; i < numeros.length; i++) {
            JButton boton = new JButton(numeros[i]);
            
            boton.setBackground(Color.WHITE);
            boton.setBorder(new LineBorder(Color.BLACK));
            boton.setFont(new Font("Arial", Font.PLAIN, 20));
            boton.setBounds(34 + (i % 4) * 205, 41 + (i / 4) * 131, 195, 121);
            boton.addActionListener(this);
            
            contentPane.add(boton);
            botones[i] = boton;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        if (comando.equals("Reiniciar")) {
            reiniciarJuego();
        } else if (comando.equals("Pausar")) {
            pausarJuego();
        } else if (comando.equals("Reanudar")) {
            reanudarJuego();
        } else {
            JButton botonClickeado = (JButton) e.getSource();
            String texto = botonClickeado.getText();
            if (!texto.equals(" ")) {
                intercambiarConVacio(botonClickeado);
            }
        }
    }
    
    private void iniciarJuego() {
   	 Collections.shuffle(Arrays.asList(numeros));
        
        for (int i = 0; i < numeros.length; i++) {
            botones[i].setText(numeros[i]);
        }
   }

    private void reiniciarJuego() {
    	
        timer.stop(); 
        segundos = 0; 
        minutos = 0;
        actualizarTiempo();
        
        Collections.shuffle(Arrays.asList(numeros));
        
        for (int i = 0; i < numeros.length; i++) {
            botones[i].setText(numeros[i]);
        }
        
        timer.start(); 
        btnPausarReanudar.setText("Pausar");
        pausado = false;
    }
    

    private void actualizarTiempo() {
        String tiempoFormateado = String.format("Tiempo: %02d:%02d", minutos, segundos);
        lblTiempo.setText(tiempoFormateado);
    }

    private void intercambiarConVacio(JButton boton) {
        JButton botonVacio = buscarBotonVacio();
        if (esAdyacente(boton, botonVacio)) {
            String tempTexto = botonVacio.getText();
            botonVacio.setText(boton.getText());
            boton.setText(tempTexto);
        }
    }

    private boolean esAdyacente(JButton boton1, JButton boton2) {
        int index1 = Arrays.asList(botones).indexOf(boton1);
        int index2 = Arrays.asList(botones).indexOf(boton2);
        int fila1 = index1 / 4;
        int columna1 = index1 % 4;
        int fila2 = index2 / 4;
        int columna2 = index2 % 4;

        return (Math.abs(fila1 - fila2) == 1 && columna1 == columna2) ||
               (Math.abs(columna1 - columna2) == 1 && fila1 == fila2);
    }

    private JButton buscarBotonVacio() {
        for (JButton boton : botones) {
            if (boton.getText().equals(" ")) {
                return boton;
            }
        }
        return null;
    }

    private void pausarJuego() {
        pausado = true;
        timer.stop();
        btnPausarReanudar.setText("Reanudar");
    
        for (JButton boton : botones) {
            boton.setEnabled(false);
        }
    }

    private void reanudarJuego() {
        pausado = false;
        timer.start();
        btnPausarReanudar.setText("Pausar");
        
        for (JButton boton : botones) {
            boton.setEnabled(true);
        }
    }
}