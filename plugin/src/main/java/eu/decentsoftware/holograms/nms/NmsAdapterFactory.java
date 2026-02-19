package eu.decentsoftware.holograms.nms;

import eu.decentsoftware.holograms.api.utils.reflect.Version;
import eu.decentsoftware.holograms.nms.api.DecentHologramsNmsException;
import eu.decentsoftware.holograms.nms.api.NmsAdapter;
import eu.decentsoftware.holograms.shared.reflect.ReflectUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * This factory is responsible for creating the {@link NmsAdapter} for the current server version.
 *
 * <p>Create an instance of {@link NmsAdapter} via {@link #createNmsAdapter(Version)} to gain access
 * to all (currently implemented) NMS-related methods.</p>
 *
 * @author d0by
 * @see NmsAdapter
 * @since 2.9.0
 */
public class NmsAdapterFactory {

    /**
     * Initialize the {@link NmsAdapter} instance for the current server version.
     *
     * @param version The version for which the {@link NmsAdapter} should be created.
     * @return The {@link NmsAdapter}.
     * @throws DecentHologramsNmsException If the current server version is not supported
     *                                     or something goes wrong during the initialization of the service.
     * @since 2.9.0
     */
    public NmsAdapter createNmsAdapter(Version version) {
        Objects.requireNonNull(version, "version cannot be null");

        String nmsAdapterImplementationClassName = "eu.decentsoftware.holograms.nms." + version.name() + ".NmsAdapterImpl";
        try {
            Class<?> nmsAdapterImplementationClass = ReflectUtil.getClass(nmsAdapterImplementationClassName);
            if (!NmsAdapter.class.isAssignableFrom(nmsAdapterImplementationClass)) {
                throw new DecentHologramsNmsException("Nms adapter " + nmsAdapterImplementationClassName + " does not implement "
                        + NmsAdapter.class.getName());
            }
            Constructor<?> constructor = nmsAdapterImplementationClass.getDeclaredConstructor();
            return (NmsAdapter) constructor.newInstance();
        } catch (DecentHologramsNmsException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            throw new DecentHologramsNmsException("Unsupported server version: " + version.name());
        } catch (NoSuchMethodException e) {
            throw new DecentHologramsNmsException("NmsAdapter implementation is missing the default constructor: "
                    + nmsAdapterImplementationClassName);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new DecentHologramsNmsException("Failed to construct a new instance of NmsAdapter implementation: "
                    + nmsAdapterImplementationClassName, e);
        } catch (Exception e) {
            throw new DecentHologramsNmsException("Unknown error occurred while initializing NmsAdapter implementation: "
                    + nmsAdapterImplementationClassName, e);
        }
    }

}