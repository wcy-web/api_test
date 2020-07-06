package homework.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;

import javax.naming.Name;

/**
 * @author 向阳
 * @date 2020/6/15-14:47
 */
public class CaseInfo2 {
    @Excel(name="用例编号")
    private int caseId;
    @Excel(name="接口名称")
    private String name;
    @Excel(name="url")
    private String url;
    @Excel(name="请求方式")
    private String type;
    @Excel(name="用例描述")
    private String desc;
    @Excel(name="参数")
    private String params;
    @Excel(name="参数类型")
    private String contentType;

    public CaseInfo2() {
    }

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "API{" +
                "caseId=" + caseId +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", type='" + type + '\'' +
                ", desc='" + desc + '\'' +
                ", params='" + params + '\'' +
                ", contentType='" + contentType + '\'' +
                '}';
    }
}
