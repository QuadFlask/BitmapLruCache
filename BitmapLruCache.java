
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class BitmapLruCache extends LruCache<String, Bitmap> implements ImageCache {

	private static int getBitmapByteCount(Bitmap bitmap) {
		return bitmap.getRowBytes() * bitmap.getHeight();
	}

	public BitmapLruCache(int maxMemoryCacheSizeInBytes) {
		super(maxMemoryCacheSizeInBytes);
	}

	public Bitmap getBitmap(String url) {
		return get(url);
	}

	public void putBitmap(String url, Bitmap bitmap) {
		put(url, bitmap);
	}

	private static long getHeapSize() {
		return Runtime.getRuntime().maxMemory();
	}

	protected int sizeOf(String key, Bitmap value) {
		return getBitmapByteCount(value);
	}

	@Override
	protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
		super.entryRemoved(evicted, key, oldValue, newValue);
		if (oldValue != null) oldValue.recycle();
		remove(key);
	}

	public static int detectCacheSize() {
		final float DEFAULT_MEMORY_CACHE_HEAP_RATIO = 1f / 8f;
		final float MAX_MEMORY_CACHE_HEAP_RATIO = 0.75f;
		final float percentageOfHeap = DEFAULT_MEMORY_CACHE_HEAP_RATIO;
		int size = Math.round(getHeapSize() * Math.min(percentageOfHeap, MAX_MEMORY_CACHE_HEAP_RATIO));
		return size;
	}
}
