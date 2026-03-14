package com.appdashboard.features.Application;

import com.appdashboard.core.BaseEntity;
import com.appdashboard.features.Store.Store;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "applications")
public class Application extends BaseEntity {

    @Column(nullable = false)
    private String name;

    private String icon;

    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, fetch = jakarta.persistence.FetchType.EAGER)
    private List<Store> stores = new ArrayList<>();
}
