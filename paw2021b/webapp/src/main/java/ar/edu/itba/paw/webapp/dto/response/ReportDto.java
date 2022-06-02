package ar.edu.itba.paw.webapp.dto.response;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.Reports;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlType;
import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

@XmlType(name= "")
public class ReportDto {

    public static Collection<ReportDto> mapReportToDto(Collection<Reports> reports, UriInfo uriInfo){
        return reports.stream().map(c->new ReportDto(uriInfo,c)).collect(Collectors.toList());
    }

    public static UriBuilder getReportUriBuilder(Reports report, UriInfo uriInfo){
        return uriInfo.getBaseUriBuilder().path("reports").path(String.valueOf(report.getIdReport()));
    }

    private Long idReport;
    private String desc;
    private UserDto reported;
    private UserDto reporter;
    private String url;
    private CommentDto comment;
    private LocalDate date;

    public ReportDto() {
    }

    public ReportDto(UriInfo uri, Reports report){
        this.reporter = new UserDto(uri, report.getReporterUser());
        this.reported = new UserDto(uri, report.getReportedUser());
        this.desc = report.getDescription();
        this.idReport = report.getIdReport();
        this.url = getReportUriBuilder(report, uri).build().toString();
        this.comment = new CommentDto(uri, report.getComment());
        this.date = report.getDate();
    }

    public Long getIdReport() {
        return idReport;
    }

    public void setIdReport(Long idReport) {
        this.idReport = idReport;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public UserDto getReported() {
        return reported;
    }

    public void setReported(UserDto reported) {
        this.reported = reported;
    }

    public UserDto getReporter() {
        return reporter;
    }

    public void setReporter(UserDto reporter) {
        this.reporter = reporter;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public CommentDto getComment() {
        return comment;
    }

    public void setComment(CommentDto comment) {
        this.comment = comment;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
