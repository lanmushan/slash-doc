package site.lanmushan.slashdocstarter.context;

import java.util.Optional;

/**
 * @author dy
 */
public class SlashDocContextThreadLocal {
    private final static ThreadLocal<SlashDocContext> slashDocContextThreadLocal = new ThreadLocal<>();

    public static Optional<SlashDocContext> get() {
        return Optional.ofNullable(slashDocContextThreadLocal.get());
    }

    public static void set(SlashDocContext slashDocContext) {
        slashDocContextThreadLocal.set(slashDocContext);
    }

    public static void remove() {
        slashDocContextThreadLocal.remove();
    }

}
