package thread;

/**
 * All objects which a thread will need access to
 * will be instantiated in thread context.
 *
 */
public class ThreadContext {
    private static ThreadLocal threadLocal = new ThreadLocal() {
        @Override
        protected ThreadContext initialValue() {
            return new ThreadContext();
        }
    };

    public static ThreadContext get() {
        return (ThreadContext) threadLocal.get();
    }

}
