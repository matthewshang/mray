package math;

public class Sampler
{
	public static Vector3f sampleSphere(Vector3f normal, float radius, float phi1, float phi2)
	{
		Vector3f tangent;
		if (normal.getX() == 0f)
		{
			tangent = new Vector3f(1f, 0f, 0f);
		}
		else if (normal.getY() == 0f)
		{
			tangent = new Vector3f(0f, 1f, 0f);
		}
		else if (normal.getZ() == 0f)
		{
			tangent = new Vector3f(0f, 0f, 1f);
		}
		else
		{
			tangent = new Vector3f(0f, -1 * normal.getZ(), normal.getY());
		}
		tangent.normalize();
		Vector3f bittangent = tangent.cross(normal);
		Vector3f sampled = sampleUnitSphere(phi1, phi2);
		Vector3f transformed = tangent.mul(sampled.getX()).add(normal.mul(sampled.getY())).add(bittangent.mul(sampled.getZ()));
		return transformed.normalize().mul(radius);
	}

	private static Vector3f sampleUnitSphere(float phi1, float phi2)
	{
		float theta = 2 * (float) Math.PI * (float) Math.random();
		float phi = (phi2 - phi1) * (float) Math.random() + phi1;
		float sinPhi = (float) Math.sin(phi);
		return new Vector3f((float) Math.cos(theta) * sinPhi, 
							(float) Math.cos(phi),
							(float) Math.sin(theta) * sinPhi);
	}
}