package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reports")
public class Reports {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reports_id_seq")
    @SequenceGenerator(sequenceName = "reports_id_seq", name = "reports_id_seq", allocationSize = 1)
    @Column(name = "id_report")
    private Long idReport;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reported_user_id")
    private User reportedUser;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reporter_user_id")
    private User reporterUser;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @Column(name = "date")
    private LocalDate date;

    /* default */
    protected Reports() {
        // Just for Hibernate
    }

    public Reports(User reportedUser, User reporterUser, String description, Comment comment) {
        this.reportedUser = reportedUser;
        this.reporterUser = reporterUser;
        this.description = description;
        this.comment = comment;
        this.date = LocalDate.now();
    }

    public Long getIdReport() {
        return idReport;
    }

    public void setIdReport(Long idReport) {
        this.idReport = idReport;
    }

    public User getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(User reportedUser) {
        this.reportedUser = reportedUser;
    }

    public User getReporterUser() {
        return reporterUser;
    }

    public void setReporterUser(User reporterUser) {
        this.reporterUser = reporterUser;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
