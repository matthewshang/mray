package light;

import math.Vector3f;
import math.Ray;
import prim.Intersection;

public interface Light
{
	public Vector3f getPointOn(Vector3f from);
	public Intersection intersect(Ray ray);
	public boolean canBeSampled();
	public Vector3f getPosition();
	public Vector3f getColor();
	public float getPower();
}