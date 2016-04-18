public class Utils {

	public static Long nonce() {
		Long ret = null;
		ret = System.currentTimeMillis();
		return ret;
	}

	public static Long getLeft(Long l) {
		return l >>> 32;
	}

	public static Long getRight(Long l) {
		return (l << 32) >>> 32;
	}
	
	public static Long random64() {
		Long r = (long) 0;
		for (int i = 0; i < 63; i++) {
			r = r << 1;
			if (Math.random() > 0.5) {
				r = r | 1;
			}
		}
		return r;
	}
}