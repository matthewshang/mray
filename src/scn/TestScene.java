package scn;

import math.Vector3f;
import matl.Material;
import matl.PhongMaterial;
import matl.MirrorMaterial;
import prim.Cylinder;
import prim.Light;
import prim.Plane;
import prim.Sphere;

// Temperory - Will put in scene describing text file parsing in later
public class TestScene
{
	public static Scene ballAndPlane()
	{
		Scene scene = new Scene();

		PhongMaterial m1 = new PhongMaterial(new Vector3f(0.6f, 1f, 0.5f), 20, new Vector3f(255f, 255f, 255f));
		MirrorMaterial m2 = new MirrorMaterial(new Vector3f(255f, 255f, 255f), new Vector3f(1f, 1f, 1f), 0.8f, 0.2f);

		scene.addObject(new Plane(new Vector3f(0f, -2f, 0f), new Vector3f(0f, 1f, 0f), m1));
		scene.addObject(new Sphere(new Vector3f(0f, 0f, 10f), 2f, m2));
		scene.addObject(new Sphere(new Vector3f(3f, -1f, 7f), 1f, m2));
		scene.addObject(new Sphere(new Vector3f(-3f, -1f, 7f), 1f, m2));

		scene.addLight(new Light(new Vector3f(-7f, 3f, 0f), 40f, new Vector3f(255f, 153f, 51f)));
		scene.addLight(new Light(new Vector3f(7f, 3f, 0f), 40f, new Vector3f(51f, 153f, 255f)));

		return scene;
	}

	// public static Scene ballCube()
	// {
	// 	Scene scene = new Scene();

	// 	// Material material = new Material(new Vector3f(0.4f, 1f, 0.5f), 10, new Vector3f(255f, 255f, 255f), false);

	// 	for (int i = 0; i < 4; i++)
	// 	{
	// 		for (int j = 0; j < 4; j++)
	// 		{
	// 			for (int k = 0; k < 4; k++)
	// 			{
	// 				scene.addObject(new Sphere(new Vector3f(-3f + i * 2f,
	// 														-3f + j * 2f,
	// 														 7f + k * 2f), 1f,
	// 										   material));
	// 			}
	// 		}
	// 	}

	// 	scene.addLight(new Light(Vector3f.zero(), 20f, new Vector3f(255f, 0f, 0f)));
	// 	scene.addLight(new Light(new Vector3f(-8f, 0f, 8f), 20f, new Vector3f(0f, 255f, 0f)));
	// 	scene.addLight(new Light(new Vector3f(8f, 0f, 8f), 20f, new Vector3f(0f, 0f, 255f)));

	// 	return scene;
	// }
}