package com.rskytech.report.word.reportLhirf.utils;

import java.awt.Color;
import java.util.List;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Table;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.CusInterval;
import com.rskytech.pojo.Lh4;
import com.rskytech.pojo.Lh5;
import com.rskytech.pojo.LhMain;
import com.rskytech.pojo.TaskMsg;

public class ReportLh5 extends LhirfReportBase {

	private Lh4 lh4;
	private Lh5 lh5;
	private List<TaskMsg> list;
	private List<CusInterval> cusList;
	private IComAreaBo comAreaBo;

	public ReportLh5(Document document, ComModelSeries ms,
			ComArea area, LhMain lhMain, String effective, Lh4 lh4, Lh5 lh5,
			List<TaskMsg> list, List<CusInterval> cusList, IComAreaBo comAreaBo) {
		super(document, ms, area, lhMain, effective);
		this.lh4 = lh4;
		this.lh5 = lh5;
		this.list = list;
		this.cusList = cusList;
		this.comAreaBo = comAreaBo;
	}

	@Override
	public Table getTableContent() throws Exception {
		Table ta = setTableAndColumn(1, null);

		ta.insertTable(getTopTable());
		ta.insertTable(getMiddleTable());
		ta.insertTable(getBottomTable());
		return ta;
	}

	public Table getTopTable() throws Exception {
		int level = lh4.getResult();

		int size = 0;
		float width = 0.7f;
		if (cusList != null) {
			size = cusList.size();
			width = width / (size + 1);
		}

		float[] columnWidth = new float[size + 2];
		columnWidth[0] = 0.3f;
		columnWidth[1] = width;
		for (int i = 0; i < size; i++) {
			columnWidth[i + 2] = width;
		}

		Table ta = setTableAndColumn(size + 2, columnWidth);

		ta.addCell(setCell(
				"L/Hirf防护功能部件退化(包含局部区域的共同模式)结合L/Hirf事件是否会妨碍飞机持续安全飞行或着落:\n"
						+ getYesOrNo(lh4.getIsSafe()), fontNowTitle,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, 3));

		ta.addCell(setCell("任务间隔的确定", fontNowTitle, Element.ALIGN_CENTER,
				Element.ALIGN_MIDDLE, size == 0 ? null : size + 1, null));

		ta.addCell(setCell("敏感度等级ED/AD", fontNowTitle));

		if (cusList != null) {
			for (CusInterval c : cusList) {
				ta.addCell(setCell(getStr(c.getIntervalLevel()), fontNowNormal,
						isColor(level, c.getIntervalLevel())));
			}
		}

		ta.addCell(setCell("区间值(FH/YR)", fontNowTitle));

		if (cusList != null) {
			for (CusInterval c : cusList) {
				ta.addCell(setCell(getStr(c.getIntervalValue()), fontNowNormal,
						isColor(level, c.getIntervalLevel())));
			}
		}
		return ta;
	}

	public Table getMiddleTable() throws Exception {
		Table ta = setTableAndColumn(3, new float[] { 0.3f, 0.15f, 0.55f });

		ta.addCell(setCell("任务类型确定", fontNowTitle, Element.ALIGN_CENTER,
				Element.ALIGN_MIDDLE, 3, null));

		ta.addCell(setCell("任务有效性判断", fontNowTitle));
		ta.addCell(setCell("是 或 否", fontNowTitle));
		ta.addCell(setCell("原因", fontNowTitle));

		ta.addCell(setCell("GVI检查是否有效", fontNowNormal));
		ta.addCell(setCell(getStr(getYesOrNo(lh5.getGviAvl())), fontNowNormal));
		ta.addCell(setCell(getStr(lh5.getGviDesc()), fontNowNormal));

		ta.addCell(setCell("DET检查是否有效", fontNowNormal));
		ta.addCell(setCell(getStr(getYesOrNo(lh5.getDetAvl())), fontNowNormal));
		ta.addCell(setCell(getStr(lh5.getDetDesc()), fontNowNormal));

		ta.addCell(setCell("FNC检查是否有效", fontNowNormal));
		ta.addCell(setCell(getStr(getYesOrNo(lh5.getFncAvl())), fontNowNormal));
		ta.addCell(setCell(getStr(lh5.getFncDesc()), fontNowNormal));

		ta.addCell(setCell("DIS检查是否有效", fontNowNormal));
		ta.addCell(setCell(getStr(getYesOrNo(lh5.getDisAvl())), fontNowNormal));
		ta.addCell(setCell(getStr(lh5.getDisDesc()), fontNowNormal));

		ta.addCell(setCell("是否需要重新设计", fontNowNormal));
		ta.addCell(setCell(getStr(getYesOrNo(lh5.getNeedRedesign())),
				fontNowNormal));
		ta.addCell(setCell(getStr(lh5.getRedesignDesc()), fontNowNormal));
		return ta;
	}

	public Table getBottomTable() throws Exception {
		Table ta = setTableAndColumn(getCol(), getColWidth());

		ta.addCell(setCell("任务", fontNowTitle, Element.ALIGN_CENTER,
				Element.ALIGN_MIDDLE, 8, null));

		ta.addCell(setCell("MSG-3任务编号", fontNowTitle));
		ta.addCell(setCell("任务类型", fontNowTitle));
		ta.addCell(setCell("间隔值", fontNowTitle));
		ta.addCell(setCell("任务描述", fontNowTitle));
		ta.addCell(setCell("接近方式", fontNowTitle));
		ta.addCell(setCell("是否区域候选任务", fontNowTitle));
		ta.addCell(setCell("转入区域", fontNowTitle));
		ta.addCell(setCell("转移原因", fontNowTitle));

		if (list != null && list.size() > 0) {
			for (TaskMsg t : list) {
				ta.addCell(setCell(getStr(t.getTaskCode()), fontNowNormal));
				ta.addCell(setCell(getStr(t.getTaskType()), fontNowNormal));
				ta.addCell(setCell(getStr(t.getTaskInterval()), fontNowNormal));
				ta.addCell(setCell(getStr(t.getTaskDesc()), fontNowNormal));
				ta.addCell(setCell(getStr(t.getReachWay()), fontNowNormal));
				ta.addCell(setCell(getStr(getYesOrNo(t.getNeedTransfer())),
						fontNowNormal));
				ta.addCell(setCell(getStr(this.comAreaBo.getAreaCodeByAreaId(t
						.getOwnArea())), fontNowNormal));
				ta.addCell(setCell(getStr(t.getWhyTransfer()), fontNowNormal));
			}
		} else {
			for (int i = 0; i < getCol(); i++) {
				ta.addCell(setCell("", fontNowNormal));
			}
		}

		return ta;
	}

	public Color isColor(Integer now, int param) {
		if (now == param) {
			return bg;
		}
		return null;
	}

	@Override
	public int getCol() {
		return 8;
	}

	@Override
	public float[] getColWidth() {
		return new float[] { 0.13f, 0.07f, 0.15f, 0.2f, 0.2f, 0.07f, 0.07f,
				0.11f };
	}

	@Override
	public String getTableAbbreviation() {
		return "LH-5";
	}

	@Override
	public String getTableName() {
		return "L/HIRF防护项目任务间隔及任务类型确定";
	}

}
