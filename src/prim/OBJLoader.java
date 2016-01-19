package prim;

import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import math.Vector3f;

public class OBJLoader
{
	public static VerticedGeometry loadFromFile(String filePath)
	{
		VerticedGeometry geometry = new VerticedGeometry();

		FileInputStream fileIn = null;
		BufferedReader reader = null;

		try
		{
			fileIn = new FileInputStream(filePath);
			reader = new BufferedReader(new InputStreamReader(fileIn));
			String line;

			do
			{
				line = reader.readLine();

				if (line != null && !line.trim().equals("") && !line.substring(0, 1).equals("#"))
				{
					switch (line.substring(0, 2).trim())
					{
						case "v":
							String parsed = line.substring(1);
							String[] vertexData = parsed.trim().split("\\s");
							geometry.addVertex(new Vector3f(Float.parseFloat(vertexData[0]),
															Float.parseFloat(vertexData[1]),
															Float.parseFloat(vertexData[2])));
							break;

						case "f":
							parsed = line.substring(1);
							String[] triangleData = parsed.trim().split("\\s");
							geometry.addTriangle(Integer.parseInt(triangleData[0].split("/")[0]) - 1,
												 Integer.parseInt(triangleData[1].split("/")[0]) - 1,
												 Integer.parseInt(triangleData[2].split("/")[0]) - 1);
							break;

						default:
							break;
					}
				}
			}
			while (line != null);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			System.out.println("Could not load model at " + filePath);
		}
		finally
		{
			try
			{
				if (reader != null)
				{
					reader.close();
				}

				if (fileIn != null)
				{
					fileIn.close();
				}
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			} 
		}

		return geometry;
	}
}