package com.windwoif.equilibrium.content.reactors.connection;

import com.windwoif.equilibrium.content.reactors.reactorEntity.ReactorEntity;
import com.windwoif.equilibrium.dimension.Atmosphere;
import com.windwoif.equilibrium.dimension.DimensionAtmosphere;
import net.minecraft.world.level.Level;

import java.util.*;

public class ReactorConnectionManager {
    private static final Map<Level, ReactorConnectionManager> instances = new HashMap<>();
    private final Set<ReactorConnection> connections = new HashSet<>();
    private final Map<ReactorEntity, Set<ReactorConnection>> entityMap = new HashMap<>();
    private final AtmosphereContainer atmosphereContainer;

    private ReactorConnectionManager(Level level) {
        Atmosphere atmosphere = DimensionAtmosphere.getAtmosphere(level.dimension());
        this.atmosphereContainer = new AtmosphereContainer(atmosphere);
    }

    public static ReactorConnectionManager get(Level level) {
        if (level.isClientSide) return null;
        return instances.computeIfAbsent(level, k -> new ReactorConnectionManager(level));
    }
    public AtmosphereContainer getAtmosphereContainer() {
        return atmosphereContainer;
    }

    public void addConnection(ReactorEntity a, ReactorEntity b) {
        if (a == b || hasConnection(a, b)) return;
        ReactorConnection conn = new ReactorConnection(a, b);
        connections.add(conn);
        entityMap.computeIfAbsent(a, k -> new HashSet<>()).add(conn);
        entityMap.computeIfAbsent(b, k -> new HashSet<>()).add(conn);
    }

    public void removeConnection(ReactorEntity a, ReactorEntity b) {
        ReactorConnection conn = findConnection(a, b);
        if (conn != null) {
            conn.invalidate();
            connections.remove(conn);
            entityMap.getOrDefault(a, Collections.emptySet()).remove(conn);
            entityMap.getOrDefault(b, Collections.emptySet()).remove(conn);
        }
    }

    public void removeAllConnections(ReactorEntity entity) {
        Set<ReactorConnection> set = entityMap.getOrDefault(entity, Collections.emptySet());
        for (ReactorConnection conn : new ArrayList<>(set)) {
            ReactorEntity other = conn.getOther(entity);
            removeConnection(entity, other);
        }
        entityMap.remove(entity);
    }

    public ReactorConnection designateConnection(ReactorEntity a, ReactorEntity b,
                                                 ReactorConnection.ConnectionType type,
                                                 int contactWidth, int contactHeight,
                                                 double MinY) {
        ReactorConnection conn = new ReactorConnection(a, b, type, contactWidth, contactHeight, MinY);
        connections.add(conn);
        entityMap.computeIfAbsent(a, k -> new HashSet<>()).add(conn);
        entityMap.computeIfAbsent(b, k -> new HashSet<>()).add(conn);
        return conn;
    }

    public ReactorConnection createAtmosphereConnection(ReactorEntity containerEntity,
                                                        ReactorConnection.ConnectionType type,
                                                        int contactWidth, int contactHeight,
                                                        double MinY,
                                                        boolean atmosphereBelow) {
        ReactorConnection conn = new ReactorConnection(containerEntity, getAtmosphereContainer(),
                type, contactWidth, contactHeight, MinY, atmosphereBelow);
        connections.add(conn);
        entityMap.computeIfAbsent(containerEntity, k -> new HashSet<>()).add(conn);
        return conn;
    }

    public void removeConnection(ReactorConnection conn) {
        conn.invalidate();
        connections.remove(conn);
        ReactorEntity lower = conn.getLowerEntity();
        ReactorEntity upper = conn.getUpperEntity();
        if (lower != null) {
            Set<ReactorConnection> set = entityMap.get(lower);
            if (set != null) set.remove(conn);
        }
        if (upper != null) {
            Set<ReactorConnection> set = entityMap.get(upper);
            if (set != null) set.remove(conn);
        }
    }

    public boolean hasConnection(ReactorEntity a, ReactorEntity b) {
        return findConnection(a, b) != null;
    }

    public ReactorConnection findConnection(ReactorEntity a, ReactorEntity b) {
        Set<ReactorConnection> set = entityMap.get(a);
        if (set == null) return null;
        return set.stream().filter(c -> c.contains(b)).findFirst().orElse(null);
    }

    public Set<ReactorConnection> getConnections(ReactorEntity entity) {
        return Collections.unmodifiableSet(entityMap.getOrDefault(entity, Collections.emptySet()));
    }

    public Set<ReactorConnection> getAllConnections() {
        return Collections.unmodifiableSet(connections);
    }

    public void tick() {
        atmosphereContainer.tick();
        for (ReactorConnection conn : connections) {
            if (conn.isActive()) {
                conn.update();
            }
        }
        for (ReactorConnection conn : connections) {
            if (conn.isActive()) {
                conn.apply();
            }
        }
    }

    public void debugPrintConnections() {
        System.out.println("=== Reactor Connections ===");
        for (ReactorConnection conn : connections) {
            System.out.println(conn);
        }
        System.out.println("============================");
    }
}