package com.example.kuailefupos.utils;

import android.graphics.Color;

import com.example.kuailefupos.datafragment.databillbean.DataBillSettlementDetailBean;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈泽宇 on 2020/4/28
 * Describe: 饼状图工具
 */
public class PieChartUtil {

    private static PieChartUtil pieChartUtil;
    private List<PieEntry> entries;

    public static PieChartUtil getPitChart(){
        if( pieChartUtil==null){
            pieChartUtil=new PieChartUtil();
        }
        return  pieChartUtil;
    }



//Map<String, Float> pieValues
    public void setPieChart(PieChart pieChart, List<DataBillSettlementDetailBean>expenseStaBeans , String title, String viceTitle, boolean showLegend, int[]  PIE_COLORS) {
        pieChart.setUsePercentValues(false);//设置使用百分比（后续有详细介绍）
        pieChart.getDescription().setEnabled(false);//设置描述
        pieChart.setRotationEnabled(true);//是否可以旋转
        pieChart.setHighlightPerTapEnabled(false);//点击是否放大
        pieChart.setDrawCenterText(true);//设置绘制环中文字
        pieChart.setDrawEntryLabels(false); //设置绘制入口标签
        //这个方法为true就是环形图，为false就是饼图
        pieChart.setDrawHoleEnabled(true);//环形

        pieChart.setEntryLabelColor(Color.BLACK); //描述文字的颜色
        pieChart.setEntryLabelTextSize(12);//描述文字的大小
        pieChart.setExtraOffsets(30, 0, 30, 0); //设置边距
        // 0表示摩擦最大，基本上一滑就停
        // 1表示没有摩擦，会自动转化为0.9999,及其顺滑
        pieChart.setDragDecelerationFrictionCoef(0.5f);//设置滑动时的摩擦系数（值越小摩擦系数越大）
        pieChart.setCenterText(title + "\n\n" + viceTitle);//设置环中的文字
        pieChart.setCenterTextSize(14f);//设置环中文字的大小
        pieChart.setCenterTextColor(Color.parseColor("#656565"));
        pieChart.setRotationAngle(120f);//设置旋转角度

        pieChart.setHoleRadius(85f);
        pieChart.setTransparentCircleRadius(0);//设置半透明圆环的半径,看着就有一种立体的感觉
        //设置环形中间空白颜色是白色
        pieChart.setHoleColor(Color.TRANSPARENT);
        //设置半透明圆环的颜色
        pieChart.setTransparentCircleColor(Color.WHITE);
        //设置半透明圆环的透明度
        pieChart.setTransparentCircleAlpha(110);

        //图例设置
        Legend legend = pieChart.getLegend();
        if (showLegend) {
            legend.setEnabled(true);//是否显示图例
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);//图例相对于图表横向的位置
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);//图例相对于图表纵向的位置
            legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);//图例显示的方向
            legend.setDrawInside(false);
            legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);//方向
        } else {
            legend.setEnabled(false);
        }

        //设置饼图数据
        setPieChartData(pieChart, expenseStaBeans,PIE_COLORS);
        pieChart.animateX(1400, Easing.EasingOption.EaseInOutQuad);//数据显示动画

    }
//
    //设置饼图数据
    private void setPieChartData(PieChart pieChart, List<DataBillSettlementDetailBean>expenseStaBeans , int[]  PIE_COLORS) {
        entries=new ArrayList<>();
        for (int i = 0;i<expenseStaBeans.size();i++){
           entries.add(new PieEntry(Float.valueOf(expenseStaBeans.get(i).getNum()), expenseStaBeans.get(i).getState()));
        }
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);//设置饼块之间的间隔
        dataSet.setSelectionShift(10f);//设置饼块选中时偏离饼图中心的距离
        dataSet.setColors(PIE_COLORS);//设置饼块的颜色
        dataSet.setDrawValues(false);
        dataSet.setValueTextSize(15f);
        dataSet.setValueTextColor(Color.WHITE);


        //设置数据显示方式有见图
        dataSet.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);//y轴数据显示在饼图内/外
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);//x轴数据显示在饼图内/外

        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.BLACK); // 饼状图内的字体颜色

        pieChart.setData(pieData);
        pieChart.highlightValues(null);
        pieChart.invalidate();
    }



}
