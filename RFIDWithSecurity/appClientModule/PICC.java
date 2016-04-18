import java.awt.Color;

import javax.swing.JLabel;


public class PICC {

	enum 	State { OFF, IDLE, ACTIVE, COLLISION, READY};	// state of picc device
	
	private String 	m_name;
	private JLabel 	m_jl_state;
	private JLabel 	m_jl_R;
	private State 	m_state;
	private int 	m_R;

	// for security
	private SecurityReceiver m_sreceiver;
	
	public PICC(String name, JLabel state, JLabel r, SecurityReceiver sr) {
		this.m_name = name;
		this.m_jl_state = state;
		this.m_jl_R = r;
		this.setStatus(State.OFF);
		this.m_sreceiver = sr;
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
			this.resetR();
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

	public void resetR() {
		this.m_R = 0;
		this.m_jl_R.setText("-");
	}
	
	private int generateR(int n) {
		return (int) Math.ceil(Math.random() * n);
	}
	
	public SecurityReceiver getSReceiver() {
		return this.m_sreceiver;
	}
}


class SecurityReceiver {

	private Long key;
	private Long id;
	
	private Long id2;
	private Long left_half;
	private Long right_half;
	private Long r2;

	
	public SecurityReceiver(Long i, Long k) {
		this.id 	= i;
		this.key 	= k;
	}
	
	public void encrypt(Long r1) {
		this.r2 = Utils.nonce();
		int g = Long.toBinaryString(r1 ^ r2 ^ key).hashCode();
		id2 = Long.rotateLeft(id, g % 64);//Utils.rotateLeft(id, g % 64);
		left_half = Utils.getLeft(id2 ^ g);
		right_half = Utils.getRight(id2 ^ g);
	}
	
	public Long getR2() {
		return r2;
	}

	public Long getLeftID2() {
		return left_half;
	}
	
	public boolean matchRightHalf(Long right) {
		if (right.equals(this.right_half)) {
			return true;
		}
		return false;
	}
}