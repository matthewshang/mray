package matl;

import math.Sampler;
import math.Vector3f;

public class DiffuseMaterial implements Material
{
	private static float HALF_PI = (float) Math.PI / 2f;
	private static float INV_PI = 1f / (float) Math.PI;

	private Vector3f m_color;

	public DiffuseMaterial(Vector3f color)
	{
		m_color = color;
	}

	public boolean receivesDirect(float random)
	{
		return true;
	}

	public Vector3f sample(Vector3f normal, Vector3f fromDirection, float random)
	{
		return Sampler.sampleSphere(normal, 1f, 0f, HALF_PI);
	}

	public float getIndirectAmount(float random)
	{
		return INV_PI;
	}

	public Vector3f getColor()
	{
		return m_color;
	}
}