package com.ruoyi.common.constant;

/**
 * i18n 中 messages_*.properties 中的 key
 * @author alf_work
 * @date 2023.04.07
 */
public interface ErrorMessagesConstants {

    /**
     * Param existed
     */
    public static final String PARAM_EXISTED = "param.existed";

    /**
     * Filed value invalid
     */
    public static final String FIELD_INVALID = "field.invalid";

    /**
     * Param is null
     */
    public static final String PARAM_IS_NULL = "param.is.null";

    /**
     * 问卷模板不存在
     */
    public static final String PAPER_TEMPLATE_NON_EXISTENT = "paper.template.non-existent";

    /**
     * 问卷模板评分标准信息不完整
     */
    public static final String PAPER_TEMPLATE_SCORE_STANDARD_IMPERFECT = "paper.template.score.standard.imperfect";

    /**
     * 问卷模板配分错误
     */
    public static final String PAPER_TEMPLATE_FULL_MARKS_ERROR = "paper.template.full.marks.error";

    /**
     * 问卷没有项目
     */
    public static final String PAPER_TEMPLATE_WITHOUT_ITEM = "paper.template.without.item";

    /**
     * 问卷没有次要项目
     */
    public static final String PAPER_TEMPLATE_WITHOUT_SECONDARY_ITEM = "paper.template.without.secondaryItem";

    /**
     * 问卷主要项目[{0}]下没有次要项目
     */
    public static final String PAPER_TEMPLATE_MAIN_ITEM_WITHOUT_SECONDARY_ITEM = "paper.template.mainItem.without.secondaryItem";

    /**
     * 问卷没有问题
     */
    public static final String PAPER_TEMPLATE_WITHOUT_QUESTION = "paper.template.without.question";

    /**
     * 项目[{0}] > [{1}]下没有问题
     */
    public static final String PAPER_TEMPLATE_ITEM_WITHOUT_QUESTION = "paper.template.item.without.question";

    /**
     * 配分异常，项目[{0}] > [{1}]的配分为[{2}]，其问题配分合计[{3}]
     */
    public static final String PAPER_TEMPLATE_ITEM_QUESTION_MARK_ERROR = "paper.template.item.question.mark.error";

    /**
     * 問卷不存在
     */
    public static final String PAPER_TEMPLATES_NOT_EXIST = "paper.templates.not.exist";

    /**
     * 问卷删除失败
     */
    public static final String PAPER_TEMPLATE_DELETE_FAIL = "paper.template.delete.fail";

    /**
     * 通用次要项目删除失败
     */
    public static final String PAPER_TEMPLATE_COMMON_ITEM_DELETE_FAIL = "paper.template.common.item.delete.fail";

    /**
     * 问卷已发布，禁止修改
     */
    public static final String PAPER_TEMPLATE_FORBID_MODIFY = "paper.template.forbid.modify";

    /**
     * 问卷上传失败
     */
    public static final String FILE_UPLOAD_FAIL = "file.upload.fail";

    /**
     * 文件下载失败
     */
    public static final String FILE_DOWNLOAD_FAIL = "file.download.fail";
}
