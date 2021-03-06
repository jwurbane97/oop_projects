package kr.ac.ajou.oop.launcher;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import kr.ac.ajou.oop.gamebuilder.GameBuilder;
import kr.ac.ajou.oop.managers.FileManager;
import kr.ac.ajou.oop.panels.Code;
import kr.ac.ajou.oop.panels.Guidance;
import kr.ac.ajou.oop.panels.Input;
import kr.ac.ajou.oop.panels.Situation;
import kr.ac.ajou.oop.panels.UserPanel;
import kr.ac.ajou.oop.state.GameState;
import kr.ac.ajou.oop.state.State;
import kr.ac.ajou.oop.user.User;

public class Game extends GameState implements ActionListener {
	
	private final static int LAST_LEVEL = 6;

	private JPanel contentPane;
	private JDialog dialog;
	private JButton btnSave;
	private JTextField tfName;
	private JFrame frame;
	
	private Code code;
	private Input input;
	private Guidance guidance;
	private Situation situation;
	
	private User user;
	private UserPanel userpanel;
	
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Game launcher = new Game();
					launcher.update();
					launcher.frame.setVisible(true);
				} catch (Exception e) {
					
				}
			}
		});
		
	}

	public Game() {
		frame = new JFrame("OOP Education Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(50, 50, 1100, 800);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(3, 3, 3, 3));
		frame.setContentPane(contentPane);
		
		user = new User();
		userpanel = new UserPanel(user);
		userpanel.getLblUsername().setFont(new Font("Helvetica Neue", Font.PLAIN, 20));
		userpanel.getLblLevel().setFont(new Font("Helvetica Neue", Font.PLAIN, 15));
		userpanel.getLblScore().setFont(new Font("Helvetica Neue", Font.PLAIN, 15));
		
		guidance = new Guidance();
		code = new Code();
		situation = new Situation();
		
		input = new Input(this);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane.createSequentialGroup().addContainerGap().addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(userpanel, GroupLayout.DEFAULT_SIZE, 1258, Short.MAX_VALUE).addGroup(gl_contentPane.createSequentialGroup().addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(guidance, GroupLayout.DEFAULT_SIZE, 745, Short.MAX_VALUE).addComponent(input, GroupLayout.DEFAULT_SIZE, 745, Short.MAX_VALUE).addComponent(situation, GroupLayout.DEFAULT_SIZE, 745, Short.MAX_VALUE)).addPreferredGap(ComponentPlacement.RELATED).addComponent(code, GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE))).addContainerGap()));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane.createSequentialGroup().addGap(5).addComponent(userpanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup().addComponent(guidance, GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED).addComponent(situation, GroupLayout.PREFERRED_SIZE, 321, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(input, GroupLayout.PREFERRED_SIZE, 224, GroupLayout.PREFERRED_SIZE)).addComponent(code, GroupLayout.DEFAULT_SIZE, 835, Short.MAX_VALUE)).addContainerGap()));
		
		contentPane.setLayout(gl_contentPane);
		
		setDialog();
	}

	public void setDialog() {
		dialog = new JDialog(frame, true);
		
		JPanel userInfo = new JPanel();
		JLabel hello = new JLabel("Hello!");
		JLabel childlblName = new JLabel("Your name:");
		
		hello.setFont(new Font("Helvetica Neue", Font.PLAIN, 30));
		hello.setHorizontalAlignment(SwingConstants.CENTER);
		
		childlblName.setFont(new Font("Helvetica Neue", Font.PLAIN, 15));
		tfName = new JTextField(10);
		tfName.setFont(new Font("Helvetica Neue", Font.PLAIN, 15));
		btnSave = new JButton("Save");
		btnSave.setFont(new Font("Helvetica Neue", Font.PLAIN, 15));
		btnSave.addActionListener(this);
	
		userInfo.setLayout(new BorderLayout(0, 0));
		userInfo.add(hello, BorderLayout.NORTH);
		userInfo.add(childlblName, BorderLayout.WEST);
		userInfo.add(tfName, BorderLayout.CENTER);
		userInfo.add(btnSave, BorderLayout.EAST);
		
		dialog.getContentPane().add(userInfo);
		dialog.setBounds(100, 100, 450, 300);
		dialog.setSize(400, 80);
		dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		dialog.setVisible(true);
	}
	
	public User getUser() {
		return user;
	}
	
	private void gameOver() {
		user.setGameOver(true);
		JLabel msg = new JLabel("Game ended!");
		msg.setFont(new Font("Helvetica Neue", Font.PLAIN, 15));
		JOptionPane.showMessageDialog(null, "Game ended!", "Game Over", JOptionPane.OK_OPTION);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(btnSave)){
			user.setName(tfName.getText().toString());
			user.setLevel(1);
			user.setScore(0);
			user.setGameOver(false);

			userpanel.getLblUsername().setText("Name: " + user.getName());
			dialog.setVisible(false);

			render();
			
			// Set Game state
			setID(State.STATE_GAME_INITIALIZE);
			update();
		}
	}

	@Override
	public void render() {
		code.load(user);
		guidance.load(user);
		situation.load(user);
		guidance.getLblGuidance().setText(guidance.getHint());
		code.getLblCode().setText(code.getCode());
		input.setComponents();
		situation.repaint();
		userpanel.getLblLevel().setText("Level: " + user.getLevel());
		userpanel.getLblScore().setText("Score: " + user.getScore());
	}

	@Override
	public void update() {
		JLabel msg;
		switch (getID()) {
		case State.STATE_GAME_INITIALIZE:
			msg = new JLabel("Welcome! You can play game by reading our guidance, and just typing your answer in the panel. That is all. Fighting.");
			msg.setFont(new Font("Helvetica Neue", Font.PLAIN, 15));
			JOptionPane.showMessageDialog(null, msg, "How to Play", JOptionPane.OK_OPTION);
			setID(State.STATE_GAME_PLAY);
			break;
		case State.STATE_GAME_PLAY:
			break;
		case State.STATE_ANSWER_CORRECT:
			msg = new JLabel("You're correct. Go to the next level.");
			msg.setFont(new Font("Helvetica Neue", Font.PLAIN, 15));
			JOptionPane.showMessageDialog(null, msg, "Great!", JOptionPane.OK_OPTION);
			user.setScore(user.getScore() + user.getLevel() * new Random().nextInt(100));
			setID(State.STATE_NEXT_LEVEL);
			break;
		case State.STATE_ANSWER_INCORRECT:
			msg = new JLabel("Incorrect! Try Again.");
			msg.setFont(new Font("Helvetica Neue", Font.PLAIN, 15));
			JOptionPane.showMessageDialog(null, msg, "Incorrect!", JOptionPane.OK_OPTION);
			setID(State.STATE_GAME_PLAY);
			break;
		case State.STATE_HIGH_SCORE:
			msg = new JLabel("You make the best score in this game ever before!");
			msg.setFont(new Font("Helvetica Neue", Font.PLAIN, 15));
			JOptionPane.showMessageDialog(null, msg, "Congrats!", JOptionPane.OK_OPTION);
			setID(State.STATE_GAME_OVER);
			break;
		case State.STATE_NEXT_LEVEL:
			if(user.getLevel() < Game.LAST_LEVEL){
				user.setLevel(user.getLevel() + 1);
				render();
				setID(State.STATE_GAME_PLAY);
			} else {
				userpanel.getLblScore().setText("Score: " + user.getScore());
				if(FileManager.userDataExists() && FileManager.getUserScore() < user.getScore()) setID(State.STATE_HIGH_SCORE);
				else setID(State.STATE_GAME_OVER);
			}
			break;
		case State.STATE_GAME_OVER:
			gameOver();
			try {
				FileManager.saveUser(user);
			} catch (IOException e) {
				
			}
			msg = new JLabel("Your Information is saved in the directory.");
			msg.setFont(new Font("Helvetica Neue", Font.PLAIN, 15));
			JOptionPane.showMessageDialog(null, msg, "End!", JOptionPane.OK_OPTION);
			setID(State.STATE_EXIT);
			break;
		case State.STATE_EXIT:
			resetContent();
			break;
		}
		if(getID() != State.STATE_GAME_PLAY) update();
	}

	@Override
	public void resetContent() {
		frame.setVisible(false);
		GameBuilder.main(null);
	}
}