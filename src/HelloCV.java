import java.util.ArrayList;
import java.util.List;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point;
import org.opencv.core.Point3;
import org.opencv.core.Size;
import org.opencv.core.CvType;

import ch.maybites.tools.math.la.*;
import ch.maybites.tools.threedee.Frustum;

public class HelloCV {
	public static void main(String[] args){
		int WIDTH = 1280;
		int HEIGHT = 800;

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		Intrinsics intrinsics = new Intrinsics();
				
		Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
		System.out.println("mat = " + mat.dump());
		// init needed variables according to OpenCV docs
		List<Mat> rvecs = new ArrayList<Mat>();
		List<Mat> tvecs = new ArrayList<Mat>();
		
		Mat cameraMatrix = Mat.eye(3, 3, CvType.CV_32FC1);
		double f = 30d / 180d * Math.PI * (double)WIDTH;
		cameraMatrix.put(0, 0, f);
		cameraMatrix.put(1, 1, f);
		cameraMatrix.put(0, 2, WIDTH / 2.0);
		cameraMatrix.put(1, 2, HEIGHT / 2.0);

		
		Mat distCoeffs = new Mat();
		//Mat distCoeffs = Mat.zeros(8, 1, CvType.CV_32FC1);

		System.out.println("intrinsic = " + cameraMatrix.dump());

		Size imageSize = new Size(WIDTH,HEIGHT);

		MatOfPoint3f objectPoint = new MatOfPoint3f();
		MatOfPoint2f imagePoint = new MatOfPoint2f();

		//<vertice index="3" localX="1.0" localY="-1.0" localZ="0.0" origin="true" targetX="0.6091954" targetY="0.083333336" type="1" warpX="0.43677413" warpY="0.233933"/>
		objectPoint.push_back(new MatOfPoint3f(new Point3( -1, 1, 1.5)));
		imagePoint.push_back(new MatOfPoint2f(new Point( 344.464996, 190.527679)));

		//<vertice index="1" localX="1.0" localY="1.0" localZ="0.0" targetX="0.8499361" targetY="0.46785715" type="1" warpX="0.75167936" warpY="0.6218758"/>
		objectPoint.push_back(new MatOfPoint3f(new Point3( 1, -1, 1.5)));
		imagePoint.push_back(new MatOfPoint2f(new Point( 1152.03796, 129.472916)));

		//<vertice index="5" localX="-1.0" localY="1.0" localZ="0.0" targetX="0.5644955" targetY="0.73333335" type="1" warpX="0.5534902" warpY="0.72509766"/>
		objectPoint.push_back(new MatOfPoint3f(new Point3( -1, -1, 0)));
		imagePoint.push_back(new MatOfPoint2f(new Point( 779.265503, 732.196105)));

		//<vertice index="7" localX="-1.0" localY="-1.0" localZ="0.0" targetX="0.3192848" targetY="0.40833333" type="1" warpX="0.3304361" warpY="0.65555406"/>
		objectPoint.push_back(new MatOfPoint3f(new Point3( 1, -1, 0)));
		imagePoint.push_back(new MatOfPoint2f(new Point( 1087.99011, 426.179779)));

		//<vertice index="6" localX="-1.0" localY="-1.0" localZ="1.5" targetX="0.26947638" targetY="0.7619048" type="1" warpX="0.27498624" warpY="0.89778674"/>
		objectPoint.push_back(new MatOfPoint3f(new Point3( -1, -1, 1.5)));
		imagePoint.push_back(new MatOfPoint2f(new Point( 784.926758, 467.272278)));

		//<vertice index="2" localX="1.0" localY="-1.0" localZ="1.5" targetX="0.61366534" targetY="0.41666666" type="1" warpX="0.50000006" warpY="0.7956562"/>
		objectPoint.push_back(new MatOfPoint3f(new Point3( -1, 1, 0)));
		imagePoint.push_back(new MatOfPoint2f(new Point( 407.898346, 473.740112)));

		//<vertice index="0" localX="1.0" localY="1.0" localZ="1.5" targetX="0.899106" targetY="0.8357143" type="1" warpX="0.84276885" warpY="0.87778014"/>
		objectPoint.push_back(new MatOfPoint3f(new Point3(1, 1, 0)));
		imagePoint.push_back(new MatOfPoint2f(new Point( 722.091431, 212.235229)));

		List<Mat> objectPoints = new ArrayList<Mat>(1);
		List<Mat> imagePoints = new ArrayList<Mat>(1);

		objectPoints.add(objectPoint);
		imagePoints.add(imagePoint);

		// calibrate!
		for(int i = 0; i< 7; i++){
			double[] vec = objectPoints.get(0).get(i, 0);
			System.out.print("pre objectPoints: row=" + i + " = [");
			for(int j = 0; j < vec.length; j ++){
				System.out.print(" " + vec[j]);					
			}
			System.out.println("]");	
		}
		for(int i = 0; i< 7; i++){
			double[] vec = imagePoints.get(0).get(i, 0);
			System.out.print("pre imagePoints: row=" + i + " = [");
			for(int j = 0; j < vec.length; j ++){
				System.out.print(" " + vec[j]);					
			}
			System.out.println("]");	
		}
		int flags =
				Calib3d.CALIB_USE_INTRINSIC_GUESS |
				Calib3d.CALIB_RECOMPUTE_EXTRINSIC |
				Calib3d.CALIB_FIX_SKEW |
				Calib3d.CALIB_FIX_ASPECT_RATIO |
				Calib3d.CALIB_FIX_K2 |
				Calib3d.CALIB_FIX_K3 |
				Calib3d.CALIB_FIX_K4 |
				Calib3d.CALIB_ZERO_TANGENT_DIST;

		System.out.println("pre intrinsic: = " + cameraMatrix.dump());

		//Calib3d.CALIB_USE_INTRINSIC_GUESS
		Calib3d.calibrateCamera(objectPoints, imagePoints, imageSize, cameraMatrix, distCoeffs, rvecs, tvecs, flags);

		System.out.println("rvecs = " + rvecs.get(0).dump());
		System.out.println("tvecs = " + tvecs.get(0).dump());
		System.out.println("post intrinsic: = " + cameraMatrix.dump());

		Matrix4x4d viewMatrix = makeViewMatrix(rvecs.get(0), tvecs.get(0));

		Matrix4x4d modelMatrix = Matrix4x4d.inverse(viewMatrix);
		System.out.println("result: " + modelMatrix.toString());
		System.out.println("translation = " + modelMatrix.get()[12] + ", " + modelMatrix.get()[13] + ", " + modelMatrix.get()[14]);
		
		intrinsics.setup(cameraMatrix, imageSize, imageSize);
		Frustum frustum = intrinsics.getFrustum(0.1f, 10.0f);
		
		
		System.out.println("frustum: " + frustum.toString());
	}

	public static Matrix4x4d makeViewMatrix(Mat rot, Mat trans){
		// http://answers.opencv.org/question/23089/opencv-opengl-proper-camera-pose-using-solvepnp/
		
		// first get the rotation values into a rotation matrix
		Mat rotation = new Mat(3, 3, CvType.CV_32FC1);
		if(rot.rows() == 3 && rot.cols() == 3) {
			rotation = rot;
		} else {
			Calib3d.Rodrigues(rot, rotation);
		}
		
		// openCV's matrices are ROW MAJOR, so we first comply with that:
		Matrix4x4d viewMatrix = new Matrix4x4d(
				rotation.get(0, 0)[0], rotation.get(0, 1)[0], rotation.get(0, 2)[0], trans.get(0, 0)[0],
				rotation.get(1, 0)[0], rotation.get(1, 1)[0], rotation.get(1, 2)[0], trans.get(1, 0)[0],
				rotation.get(2, 0)[0], rotation.get(2, 1)[0], rotation.get(2, 2)[0], trans.get(2, 0)[0],
				0, 0, 0, 1.0d);
		
		// then we need a transfer matrix, since openCL has Y and Z axis inverted
		Matrix4x4d cvToGl = new Matrix4x4d();
		cvToGl.setElement(0, 0, 1);
		cvToGl.setElement(1, 1, -1); // Invert the y axis
		cvToGl.setElement(2, 2, -1); // invert the z axis
		cvToGl.setElement(3, 3, 1);
		
		// invert the y and z axis
		cvToGl.multiply(viewMatrix);
		
		// and then go from ROW Major to COLUM Major by transposing
		cvToGl.transpose();
		
		return cvToGl;
	}
}