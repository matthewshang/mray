package scn;

import light.PointLight;
import light.SphereLight;
import math.Vector3f;
import matl.PhongMaterial;
import matl.MirrorMaterial;
import prim.Cylinder;
import prim.Plane;
import prim.Sphere;

// Temperory - Will put in scene describing text file parsing in later
public class TestScene
{
	public static Scene ballAndPlane()
	{
		Scene scene = new Scene();

		PhongMaterial m1 = new PhongMaterial(new Vector3f(0.6f, 1f, 0.5f), 20, new Vector3f(255f, 255f, 255f));
		MirrorMaterial m2 = new MirrorMaterial(new Vector3f(255f, 255f, 255f), new Vector3f(1f, 1f, 1f), 0f, 0.6f, 0.4f);

		scene.addObject(new Plane(new Vector3f(0f, -2f, 0f), new Vector3f(0f, 1f, 0f), m1));
		scene.addObject(new Sphere(new Vector3f(0f, 0f, 10f), 2f, m2));
		scene.addObject(new Sphere(new Vector3f(3f, -1f, 7f), 1f, m2));
		scene.addObject(new Sphere(new Vector3f(-3f, -1f, 7f), 1f, m2));

		scene.addLight(new SphereLight(new Vector3f(-7f, 3f, 0f), 40f, 2.5f, new Vector3f(255f, 153f, 51f)));
		scene.addLight(new SphereLight(new Vector3f(7f, 3f, 0f), 40f, 2.5f, new Vector3f(51f, 153f, 255f)));

		// scene.addLight(new PointLight(new Vector3f(-7f, 3f, 0f), 40f, new Vector3f(255f, 153f, 51f)));
		// scene.addLight(new PointLight(new Vector3f(7f, 3f, 0f), 40f, new Vector3f(51f, 153f, 255f)));

		return scene;
	}

	public static Scene glossyBalls()
	{
		Scene scene = new Scene();

		PhongMaterial phong = new PhongMaterial(new Vector3f(0.6f, 1f, 0.5f), 20, new Vector3f(255f, 255f, 255f));
		scene.addObject(new Plane(new Vector3f(0f, -2f, 0f), new Vector3f(0f, 1f, 0f), phong));
		for (int i = 0; i < 6; i++)
		{
			scene.addObject(new Sphere(new Vector3f(-5f + (float) i * 2f, -1f, 10f), 1f, new MirrorMaterial(new Vector3f(255f, 255f, 255f), new Vector3f(0.9f, 0.9f, 0.9f), (float) i * 0.1f, 0.8f, 0.2f)));
		}

		scene.addObject(new Sphere(new Vector3f(0f, 3f, -6f), 5f, phong));

		scene.addLight(new SphereLight(new Vector3f(-7f, 3f, 0f), 50f, 2.5f, new Vector3f(255f, 153f, 51f)));
		scene.addLight(new SphereLight(new Vector3f(7f, 3f, 0f), 50f, 2.5f, new Vector3f(51f, 153f, 255f)));

		return scene;
	}

	public static Scene coloredBalls()
	{
		Scene scene = new Scene();

		// scene.addObject(new Plane(new Vector3f(0f, -2f, 0f), new Vector3f(0f, 1f, 0f), new PhongMaterial(new Vector3f(0.7f, 1f, 0.2f), 20, new Vector3f(255f, 255f, 255f))));
		scene.addObject(new Plane(new Vector3f(0f, -2f, 0f), new Vector3f(0f, 1f, 0f), new MirrorMaterial(new Vector3f(255f, 255f, 255f), new Vector3f(1f, 1f, 1f), 0.2f, 0.8f, 0.2f)));

		scene.addObject(new Sphere(new Vector3f(0f, 0f, 10f), 2f, new MirrorMaterial(new Vector3f(255f, 102f, 102f), new Vector3f(1f, 0.4f, 0.4f), 0f, 0.4f, 0.6f)));
		scene.addObject(new Sphere(new Vector3f(4f, -0.5f, 7f), 1.5f, new MirrorMaterial(new Vector3f(178f, 255f, 102f), new Vector3f(0.7f, 1f, 0.4f), 0.1f, 0.4f, 0.6f)));
		scene.addObject(new Sphere(new Vector3f(-4f, -1f, 6f), 1f, new MirrorMaterial(new Vector3f(102f, 178f, 255f), new Vector3f(0.4f, 0.7f, 1f), 0f, 0.4f, 0.6f)));
		scene.addObject(new Sphere(new Vector3f(-2f, -1.5f, 8f), 0.5f, new MirrorMaterial(new Vector3f(255f, 255f, 102f), new Vector3f(1f, 1f, 0.4f), 0.2f, 0.4f, 0.6f)));
		scene.addObject(new Sphere(new Vector3f(1f, -1.5f, 4f), 0.4f, new MirrorMaterial(new Vector3f(204f, 153f, 255f), new Vector3f(0.8f, 0.6f, 1f), 0f, 0.4f, 0.6f)));

		scene.addLight(new SphereLight(new Vector3f(0f, 10f, -2f), 150f, 2.5f, new Vector3f(255f, 255f, 255f)));

		return scene;
	}
}