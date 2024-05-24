package ch.dennis.film.configs;

import ch.dennis.film.services.FilmController;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/resources")
public class RestConfig extends Application {
    public Set<Class<?>> getClasses() {
        return new HashSet<>(
                Arrays.asList(
                        FilmController.class,
                        AuthenticationFilter.class
                )
        );
    }
}
