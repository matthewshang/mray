public class TestScene
{

	// public static Scene scene_box()
	// {
	// 	Scene scene = new Scene();
	// 	scene.addObject(new Plane(new Vector3f(4f, 0f, 0f), new Vector3f(-1f, 0f, 0f), new Vector3f(255f, 0f, 0f)));

	// 	scene.addObject(new Plane(new Vector3f(-4f, 0f, 0f), new Vector3f(1f, 0f, 0f), new Vector3f(0f, 0f, 255f)));
	// 	scene.addObject(new Plane(new Vector3f(0f, 4f, 0f), new Vector3f(0f, -1f, 0f), new Vector3f(255f, 255f, 255f)));
	// 	scene.addObject(new Plane(new Vector3f(0f, -4f, 0f), new Vector3f(0f, 1f, 0f), new Vector3f(255f, 255f, 255f)));
	// 	scene.addObject(new Plane(new Vector3f(0f, 0f, 10f), new Vector3f(0f, 0f, -1f), new Vector3f(255f, 255f, 255f)));

	// 	scene.addObject(new Sphere(new Vector3f(0f, 0f, 7f), 1f, new Vector3f(255f, 255f, 255f)));

	// 	scene.addLight(new Light(new Vector3f(-3f, 3.9f, 5f), 7f, new Vector3f(0f, 0f, 255f)));
	// 	scene.addLight(new Light(new Vector3f(3f, 3.9f, 5f), 7f, new Vector3f(255f, 0f, 0f)));
	// 	scene.addLight(new Light(new Vector3f(0f, 0f, 0f), 3f, new Vector3f(255f, 255f, 255f)));
	// 	scene.addLight(new Light(new Vector3f(-3f, 0f, 6f), 7f, new Vector3f(255f, 255f, 255f)));

	// 	return scene;

	// }

	public static Scene box()
	{
		Scene scene = new Scene();

		scene.addObject(new Sphere(new Vector3f(0f, 0f, 5f), 1f, new Vector3f(255f, 255f, 255f)));

		scene.addLight(new Light(new Vector3f(0f, 0f, 10f), 4f, new Vector3f(255f, 0f, 0f)));

		return scene;

	}

	public static Scene ballAndPlane()
	{
		Scene scene = new Scene();

		scene.addObject(new Plane(new Vector3f(0f, -2f, 0f), new Vector3f(0f, 1f, 0f), new Vector3f(255f, 255f, 255f)));
		scene.addObject(new Sphere(new Vector3f(0f, 0f, 10f), 2f, new Vector3f(255f, 255f, 255f)));
		scene.addObject(new Sphere(new Vector3f(3f, -1f, 7f), 1f, new Vector3f(255f, 255f, 255f)));
		scene.addObject(new Sphere(new Vector3f(-3f, -1f, 7f), 1f, new Vector3f(255f, 255f, 255f)));

		scene.addLight(new Light(new Vector3f(-7f, 3f, 0f), 5f, new Vector3f(255f, 153f, 51f)));
		scene.addLight(new Light(new Vector3f(7f, 3f, 0f), 5f, new Vector3f(51f, 153f, 255f)));

		return scene;
	}

	public static Scene ballCube()
	{
		Scene scene = new Scene();

		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				for (int k = 0; k < 4; k++)
				{
					scene.addObject(new Sphere(new Vector3f(-3f + i * 2f,
															-3f + j * 2f,
															 7f + k * 2f), 1f,
											   new Vector3f(255f, 255f, 255f)));
				}
			}
		}

		scene.addLight(new Light(new Vector3f(0f, 0f, 0f), 2f, new Vector3f(255f, 0f, 0f)));
		scene.addLight(new Light(new Vector3f(-8f, 0f, 8f), 5f, new Vector3f(0f, 255f, 0f)));
		scene.addLight(new Light(new Vector3f(8f, 0f, 8f), 5f, new Vector3f(0f, 0f, 255f)));

		return scene;
	}
}