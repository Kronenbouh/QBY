package benj.math;

public final class MathUtils {
	private MathUtils() { }
	
	private static final float FLOAT_PRECISION = 0.00001f;
	
	public static boolean isZero(float f) {
		return epsilonCompare(f,0f) == 0;
	}
	
	public static int epsilonCompare(float f0, float f1) {
		return Math.abs(f0 - f1) < FLOAT_PRECISION ? 0 : Float.compare(f0,f1);
	}
}
