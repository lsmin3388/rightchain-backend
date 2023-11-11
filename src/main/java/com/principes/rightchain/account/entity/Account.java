package com.principes.rightchain.account.entity;

import com.principes.rightchain.agree.entity.Agree;
import com.principes.rightchain.report.entity.Report;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", unique = true)
    private Long id;

    @Email
    @Column(unique = true)
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    private int age;
    private String phoneNum;
    private String schoolName;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

    @Builder
    public Account(Long id, String email, String password, Role role, int age, Sex sex, String phoneNum, String schoolName, List<Report> reports) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.age = age;
        this.sex = sex;
        this.phoneNum = phoneNum;
        this.schoolName = schoolName;
        this.reports = reports;
    }
}