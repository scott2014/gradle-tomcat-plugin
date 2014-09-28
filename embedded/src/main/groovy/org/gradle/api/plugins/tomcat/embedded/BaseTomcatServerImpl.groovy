package org.gradle.api.plugins.tomcat.embedded

/**
 * Implementation of common Tomcat server logic.
 *
 * @author Benjamin Muschko
 * @author Andrey Bloschetsov
 */
abstract class BaseTomcatServerImpl implements TomcatServer {
    final tomcat
    def context
    private boolean stopped

    public BaseTomcatServerImpl() {
        Class serverClass = loadClass(getServerClassName())
        this.tomcat = serverClass.newInstance()
    }

    Class loadClass(String className) {
        ClassLoader classLoader = Thread.currentThread().contextClassLoader
        classLoader.loadClass(className)
    }

    @Override
    def getEmbedded() {
        tomcat
    }

    @Override
    def getContext() {
        context
    }

    @Override
    void addWebappResource(File resource) {
        context.loader.addRepository(resource.toURI().toURL().toString())
    }

    @Override
    void start() {
        stopped = false
        tomcat.start()
    }

    @Override
    void stop() {
        context?.stop()
        context?.destroy()

        if(!stopped) {
            tomcat.stop()
            stopped = true
        }

        tomcat.destroy()
    }

    @Override
    boolean isStopped() {
        stopped
    }
}
