package com.pad.lab.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "CALCULATIONS")


public class Calculation {
    @Id
    private Integer id;

    @Column(name = "type")
    private String type;

    @Column(name = "init")
    private Integer init;

    @Column(name = "status")
    private String status;

    @Column(name = "result")
    private Integer result;
}

