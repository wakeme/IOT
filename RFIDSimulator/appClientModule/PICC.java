import java.awt.Color;

import javax.swing.JLabel;


public class PICC {

	enum 	State { OFF, IDLE, ACTIVE, COLLISION, READY};	// transmitting state of the node
	
	private String 	m_name;
	private JLabel 	m_jl_state;
	private JLabel 	m_jl_R;
	private State 	m_state;
	private int 	m_R;
	
	public PICC(String name, JLabel state, JLabel r) {
		this.m_name = name;
		this.m_jl_state = state;
		this.m_jl_R = r;
		this.setStatus(State.OFF);
	}
	
	public void setStatus(State s) {
		this.m_state = s;
		switch(s) {
		case OFF:
			this.m_jl_state.setText("OFF");
			this.m_jl_state.setBackground(Color.GRAY);
			break;
		case IDLE:
			this.m_jl_state.setText("IDLE");
			this.m_jl_state.setBackground(Color.YELLOW);
			break;
		case ACTIVE:
			System.out.println("PICC " + this.getName() + ": IN ACTIVE STATE");
			this.m_jl_state.setText("ACTIVATE");
			this.m_jl_state.setBackground(Color.GREEN);
			break;
		case COLLISION:
			this.m_jl_state.setText("COLLISION");
			this.m_jl_state.setBackground(Color.RED);
			break;
		case READY:
			System.out.println("PICC " + this.getName() + ": READY FOR ACTIVE");
			this.m_jl_state.setText("READY");
			this.m_jl_state.setBackground(Color.BLUE);
			break;
		default:
			break;
		}
	}
	
	public State getState() {
		return this.m_state;
	}
	
	public String getName() {
		return this.m_name;
	}
	
	public boolean receiveREQ(int n) {
		if (n > 1) {
			int r = this.generateR(n);
			this.m_R = r;
			this.m_jl_R.setText(Integer.toString(r));
			System.out.println("PICC " + this.m_name + ": GENERATE RANDOM NUMBER " + r);
		} else if (n == 1) {
			System.out.println("PICC " + this.m_name + ": SEND ANSWER TO REQUEST");
		}
		return true;
	}
	
	public boolean receiveSlotMarker(int n) {
		if (n == this.m_R) {
			System.out.println("PICC " + this.m_name + ": MATCHED SLOT " + n);
			return true;
		}
		return false;
	}
	
	private int generateR(int n) {
		return (int) Math.ceil(Math.random() * n);
	}
}
