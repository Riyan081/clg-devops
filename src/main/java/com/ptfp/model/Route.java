package com.ptfp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "routes")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 32)
    private String routeCode;

    @Column(nullable = false, length = 100)
    private String routeName;

    @Column(nullable = false, length = 32)
    private String transportMode; // BUS, METRO, TRAIN

    public Route() {}

    public Route(String routeCode, String routeName, String transportMode) {
        this.routeCode = routeCode;
        this.routeName = routeName;
        this.transportMode = transportMode;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRouteCode() { return routeCode; }
    public void setRouteCode(String routeCode) { this.routeCode = routeCode; }

    public String getRouteName() { return routeName; }
    public void setRouteName(String routeName) { this.routeName = routeName; }

    public String getTransportMode() { return transportMode; }
    public void setTransportMode(String transportMode) { this.transportMode = transportMode; }
}
