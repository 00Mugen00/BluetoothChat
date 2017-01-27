/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bluetoothchatserver;

import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.bluetooth.UUID;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

/**
 *
 * @author johncarlo
 */
public class bluetoothChatPanel extends javax.swing.JFrame {

    /**
     * Creates new form bluetootChatPanel
     */
    private BluetoothChatServer btServer;
    private InputStream inputStream;
    private OutputStream outputStream;
    private StreamConnection serviceRequestManager;
    private StreamConnectionNotifier service;
    public bluetoothChatPanel() {
        //1. Creamos la URL
        String URL = "btspp://localhost:" + new UUID(0x1101).toString() + ";name=BluetoothChat"; 
        //2. Inicializamos los componentes del interfaz gráfico
        //initComponentes() es un método autogenerado por el generador de interfaz gráfico de Netbeans
        initComponents();
        //3. Inicializamos el Servidor Bluetooth
        btServer = new BluetoothChatServer(URL);
        //4. Imprimimos un mensaje por pantalla para indicar que el servidor se ha conectado con el cliente bluetooth
        this.printStatus("Server has connected to Client\n");
        //5. Inicializamos los flujos de datos
        //outputStream es necesario para enviar un mensaje desde el interfaz gráfico
        //inputStream es necesario para cerrar el flujo de entrada de datos cuando el propio cliente envía un END\n
	this.inputStream = btServer.getInputStream();
	this.outputStream = btServer.getOutputStream();
        //6. Inicializamos el serviceRequestManager
        //serviceRequestManager es necesario para 
	this.serviceRequestManager = btServer.getStreamConnection();
	this.service = btServer.getService();
        //7. Ponemos el icono al interfaz gráfico
        setIcon();
        //8. Llamamos BluetoothClientMessageReciever para recibir mensajes
	BluetoothServerMessageReciever bluetoothServerMessageReciever = new BluetoothServerMessageReciever(this.inputStream,this.outputStream,this,this.serviceRequestManager,this.service);
	bluetoothServerMessageReciever.start();
    }
    
    //El método sendMessage() es el método que se ejecutará cuando se pulsa el botón de Send
    public void sendMessage(){
        try{
            //1. Creamos el string que se imprimirá en el GUI
            String message = this.getMessage();
            //2. Enviamos el texto que está escrito en el componente messageField
            outputStream.write((message+"\n").getBytes());
            //3. Escribimos el text que está en el messageField en el componente chatArea
            this.printMessageInChat("Server", message+"\n");
            //4. Borramos el contenido del messageField
            this.clearMessageField();
            if("END".equals(message)){
		//Cuando el servidor envia un END cerramos InputStream y OutStream
		inputStream.close();
		outputStream.close();
		serviceRequestManager.close();
		service.close();
            }
	} catch(IOException e1){
            //Esta excepción salta cuando se ha cerrado la conexion y el servidor desea enviar un mensaje
            this.printMessageInChat("ERROR", "Closed connection\n");
	}        
    }
    
    //El método initComponent() ha sido generado por el generador de interfaz gráfico de netbeans
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        close = new javax.swing.JLabel();
        sendButton = new javax.swing.JLabel();
        minimize = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        chatArea = new javax.swing.JTextArea();
        messageField = new javax.swing.JTextField();
        background = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(600, 450));
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(600, 450));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        close.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        close.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                closeMouseReleased(evt);
            }
        });
        getContentPane().add(close, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 10, 20, 20));

        sendButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        sendButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                sendButtonMouseReleased(evt);
            }
        });
        getContentPane().add(sendButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(503, 380, 60, 30));

        minimize.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        minimize.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                minimizeMouseReleased(evt);
            }
        });
        getContentPane().add(minimize, new org.netbeans.lib.awtextra.AbsoluteConstraints(535, 10, 20, 20));

        jScrollPane1.setPreferredSize(new java.awt.Dimension(426, 166));

        chatArea.setColumns(20);
        chatArea.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        chatArea.setLineWrap(true);
        chatArea.setRows(5);
        chatArea.setOpaque(false);
        chatArea.setEditable(false);
        jScrollPane1.setViewportView(chatArea);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(36, 66, 530, 310));

        messageField.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        messageField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                messageFieldKeyReleased(evt);
            }
        });
        getContentPane().add(messageField, new org.netbeans.lib.awtextra.AbsoluteConstraints(38, 381, 432, 30));

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bluetoothchatserverimages/window.png"))); // NOI18N
        getContentPane().add(background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 600, 450));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    //El método closeMouseRealease es el método que escucha el evento sobre el botón de cerrar
    //Este método se ha tenido que implimentar ya que no estamos usando la biblioteca convencional de java para crear el panel
    private void closeMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeMouseReleased
        try {
            //1. Cuando el usuario pulsa el botón de cerrar del interfaz gráfica, cerramos la conexión
            // y también cerramos todos los flujos de datos
            outputStream.write(("END\n").getBytes());
            inputStream.close();
            outputStream.close();
            serviceRequestManager.close();
        } catch (IOException ex) {
            System.out.println("Program Closed");
        }
        System.exit(0);
    }//GEN-LAST:event_closeMouseReleased
    
    //El método minimizeMouseRealeased es el método que escucha el evento sobre el botón de minimizar
    //Este método se ha tenido que implimentar ya que no estamos usando la biblioteca convencional de java para crear el panel
    private void minimizeMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimizeMouseReleased
        this.setState(Frame.ICONIFIED);
    }//GEN-LAST:event_minimizeMouseReleased

    //Este método es el que se encarga de escuchar el evento sobre el botón de enviar
    //Como podemos observar, este método es el que llama al método sendMessage()
    private void sendButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sendButtonMouseReleased
        sendMessage();
    }//GEN-LAST:event_sendButtonMouseReleased

    //El método messageFieldKeyReleased es el método que escucha el evento sobre la tecla intro
    //De tal manera, que si pulsamos la tecla intro, se envía el contenido del messageField() llamando al método sendMessage
    private void messageFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_messageFieldKeyReleased
        //Si se pulsa enter que también se haga el envio
        int key = evt.getKeyCode();
            if(key == KeyEvent.VK_ENTER){
                sendMessage();
        }
    }//GEN-LAST:event_messageFieldKeyReleased
    
    //El método printStatus() lo usamos para mostrar mensajes de estado en el GUI
    public void printStatus(String status){
	chatArea.append(status);
    }
    
    //Este método sirve para obtener el texto que hay en el messageField
    public String getMessage(){
	return this.messageField.getText();
    }
    
    //Este método sirve para escribir sobre el chatArea (y asi poder mostrar la conversación en ella)
    public void printMessageInChat(String user,String message){
	if(!"".equals(message)){
            chatArea.append("\n"+user+": "+message);
	}
    }
    
    //Este método sirve para limpiar el messageField
    public void clearMessageField(){
	messageField.setText("");
    }
    
    //Este método sirve para poner el icono a la aplicación
    private void setIcon(){
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/bluetoothchatserverimages/server.png")));
    }
    
    /**
     * @param args the command line arguments
     */
    //El método principal crea la instancia de bluetoothChatPanel y lo pone visible
    //Este método también es auto generado por el generador de interfaz gráfico de Netbeans
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(bluetoothChatPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(bluetoothChatPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(bluetoothChatPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(bluetoothChatPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new bluetoothChatPanel().setVisible(true);
            }
        });
    }
    //A continuación, podemos encontrar la declaración de las variables que representan los 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel background;
    private javax.swing.JTextArea chatArea;
    private javax.swing.JLabel close;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField messageField;
    private javax.swing.JLabel minimize;
    private javax.swing.JLabel sendButton;
    // End of variables declaration//GEN-END:variables
}
