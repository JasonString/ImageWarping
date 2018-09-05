package jason;

import java.awt.EventQueue;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ResizingWindow {

	private JFrame frmResizing;

	/**
	 * Launch the application.
	 */
	public static void NewScreen(BufferedImage image) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ResizingWindow window = new ResizingWindow(image);
					window.frmResizing.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ResizingWindow(BufferedImage image) {
		initialize(image);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(BufferedImage image) {
		frmResizing = new JFrame();
		frmResizing.setTitle("Resizing");
		frmResizing.setBounds(100, 100, 18+image.getWidth()*2, 35+image.getHeight()*2);
		frmResizing.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmResizing.getContentPane().setLayout(null);
		
		JLabel lblResizingimage = new JLabel("");
		lblResizingimage.setBounds(0, 0, image.getWidth()*2, image.getHeight()*2);
		lblResizingimage.setIcon(new ImageIcon(image));
		frmResizing.getContentPane().add(lblResizingimage);
	}
}
