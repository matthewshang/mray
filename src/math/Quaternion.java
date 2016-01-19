package math;

public class Quaternion
{
	private Vector3f m_v;
	private float m_w;

	public Quaternion()
	{
		m_v = Vector3f.zero();
		m_w = 1f;
	}

	public Quaternion(Vector3f imaginary, float real)
	{
		m_v = imaginary;
		m_w = real;
	}

	public Quaternion setFromAxisAngle(Vector3f axis, float angle)
	{
		m_v = axis.normalize().mul((float) Math.sin(angle / 2f));
		m_w = (float) Math.cos(angle / 2f);
		return this;
	}

	public Vector3f getImaginary()
	{
		return m_v;
	}

	public float getReal()
	{
		return m_w;
	}

	public Quaternion multiply(Quaternion q)
	{
		Vector3f qv = q.getImaginary();
		float qw = q.getReal();
		return new Quaternion(m_v.cross(qv).add(qv.getMul(m_w)).add(m_v.getMul(qw)), m_w * qw - qv.dot(m_v));
	}

	public Quaternion conjugate()
	{
		return new Quaternion(m_v.getMul(-1f), m_w);
	}
}