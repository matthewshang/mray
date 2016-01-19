package math;

import java.util.Arrays;

public class Matrix4f
{
	private float[][] m;

	public Matrix4f()
	{
		m = new float[4][4];
		initIdentity();
	}

	// Real-Time Rendering (Third Edition) - page 76
	public Matrix4f(Quaternion q)
	{
		m = new float[4][4];
		initIdentity();

		Vector3f qv = q.getImaginary();
		float qx = qv.getX();
		float qy = qv.getY();
		float qz = qv.getZ();
		float qw = q.getReal();

		m[0][0] = 1f - 2f * (qy * qy + qz * qz); m[0][1] = 2f * (qx * qy - qw * qz);      m[0][2] = 2f * (qx * qz + qw * qy);
		m[1][0] = 2f * (qx * qy + qw * qz);      m[1][1] = 1f - 2f * (qx * qx + qz * qz); m[1][2] = 2f * (qy * qz - qw * qx);
		m[2][0] = 2f * (qx * qz - qw * qy);      m[2][1] = 2f * (qy * qz + qw * qx);      m[2][2] = 1f - 2f * (qx * qx + qy * qy);
	}

	public void set(int i, int j, float value)
	{
		m[i][j] = value;
	}

	public float get(int i, int j)
	{
		return m[i][j];
	}

	public Matrix4f initIdentity()
	{
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				m[i][j] = (i == j) ? 1f : 0f;
			}
		}

		return this;
	}

	public Matrix4f initScale(Vector3f scale)
	{
		m[0][0] = scale.getX();
		m[1][1] = scale.getY();
		m[2][2] = scale.getZ();

		return this;
	}

	public Matrix4f initTranslate(Vector3f translation)
	{
		m[0][3] = translation.getX();
		m[1][3] = translation.getY();
		m[2][3] = translation.getZ();

		return this;
	}

	public Matrix4f getTranspose()
	{
		Matrix4f t = new Matrix4f();

		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				t.set(j, i, m[i][j]);
			}
		}

		return t;
	}

	public Matrix4f getInverse()
	{
		return getAdjoint().scale(1f / getDet());
	}

	private Matrix4f getAdjoint()
	{
		Matrix4f a = new Matrix4f();

		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				a.set(i, j, ((i + j) % 2 == 0 ? 1f : -1f) * getSubdet(j, i));
			}
		}

		return a;
	}

	public float getSubdet(int i, int j)
	{
		float[][] d = new float[3][3];
		int k = 0;
		int l = 0;
		for (int a = 0; a < 4; a++)
		{
			l = 0;

			if (a == i)
			{
				continue; 
			}

			for (int b = 0; b < 4; b++)
			{
				if (b == j)
				{
					continue;
				}

				d[k][l] = m[a][b];
				l++;
			}

			k++;
		}

		return (d[0][0] * d[1][1] * d[2][2]) + (d[0][1] * d[1][2] * d[2][0]) + (d[0][2] * d[1][0] * d[2][1])
			 - (d[0][2] * d[1][1] * d[2][0]) - (d[0][1] * d[1][0] * d[2][2]) - (d[0][0] * d[1][2] * d[2][1]);
	}

	private float getDet()
	{
		return (m[0][0] * m[1][1] * m[2][2] * m[3][3]) + (m[0][0] * m[1][2] * m[2][3] * m[3][1]) + (m[0][0] * m[1][3] * m[2][1] * m[3][2]) 
			 + (m[0][1] * m[1][0] * m[2][3] * m[3][2]) + (m[0][1] * m[1][2] * m[2][0] * m[3][3]) + (m[0][1] * m[1][3] * m[2][2] * m[3][0])
			 + (m[0][2] * m[1][0] * m[2][1] * m[3][3]) + (m[0][2] * m[1][1] * m[2][3] * m[3][0]) + (m[0][2] * m[1][3] * m[2][0] * m[3][1])
			 + (m[0][3] * m[2][0] * m[2][2] * m[3][1]) + (m[0][3] * m[1][1] * m[2][0] * m[3][2]) + (m[0][3] * m[1][2] * m[2][1] * m[3][0])

			 - (m[0][0] * m[1][1] * m[2][3] * m[3][2]) - (m[0][0] * m[1][2] * m[2][1] * m[3][3]) - (m[0][0] * m[1][3] * m[2][2] * m[3][1])
			 - (m[0][1] * m[1][0] * m[2][2] * m[3][3]) - (m[0][1] * m[1][2] * m[2][3] * m[3][0]) - (m[0][1] * m[1][3] * m[2][0] * m[3][2])
			 - (m[0][2] * m[1][0] * m[2][3] * m[3][1]) - (m[0][2] * m[1][1] * m[2][0] * m[3][3]) - (m[0][2] * m[1][3] * m[2][1] * m[3][0])
			 - (m[0][3] * m[1][0] * m[2][1] * m[3][2]) - (m[0][3] * m[1][1] * m[2][2] * m[3][0]) - (m[0][3] * m[1][2] * m[2][0] * m[3][1]);
	}

	public Matrix4f multiply(Matrix4f a)
	{
		Matrix4f r = new Matrix4f();

		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				r.set(i, j, m[i][0] * a.get(0, j) + 
					        m[i][1] * a.get(1, j) +
					        m[i][2] * a.get(2, j) +
					        m[i][3] * a.get(3, j));
			}
		}

		return r;
	}

	public Matrix4f scale(float s)
	{
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				m[i][j] *= s;
			}
		}

		return this;
	}

	public Vector3f apply(Vector3f v, float w)
	{
		return new Vector3f(m[0][0] * v.getX() + m[0][1] * v.getY() + m[0][2] * v.getZ() + m[0][3] * w,
							m[1][0] * v.getX() + m[1][1] * v.getY() + m[1][2] * v.getZ() + m[1][3] * w,
							m[2][0] * v.getX() + m[2][1] * v.getY() + m[2][2] * v.getZ() + m[2][3] * w);
	}

	public void print()
	{
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				System.out.print(m[i][j] + "    ");
			}
			System.out.println();
		}

		System.out.println();
	}
}