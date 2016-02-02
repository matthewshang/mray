package matl;

import math.Vector3f;
import math.Sampler;

public class MirrorMaterial implements Material
{
	private static float HALF_PI = (float) Math.PI / 2f;
	private static float INV_PI = 1f / (float) Math.PI;

	private Vector3f m_color;
	private float m_glossiness;
	private float m_reflectedAmount;

	public MirrorMaterial(Vector3f color, float glossiness, float reflectedAmount)
	{
		m_color = color;
		m_glossiness = glossiness;
		m_reflectedAmount = reflectedAmount;
	}

	public boolean receivesDirect(float random)
	{
		if (random < m_reflectedAmount)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public Vector3f getColor()
	{
		return m_color;
	}

	public float getIndirectAmount(float random)
	{
		if (random < m_reflectedAmount)
		{
			return 1f;
		}
		else
		{
			return INV_PI;
		}
	}

	public Vector3f sample(Vector3f normal, Vector3f fromDirection, float random)
	{
		if (random < m_reflectedAmount)
		{
			Vector3f reflectedNormal = normal.reflect(fromDirection).mul(-1f).normalize();
			return Sampler.sampleSphere(reflectedNormal, 1f, 0f, HALF_PI * m_glossiness);
		}
		else
		{
			return Sampler.sampleSphere(normal, 1f, 0f, HALF_PI);
		}
	}
}