package com.dream.util;


import com.dream.util.pojo.Point;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 判断经纬度位置是否在一个不规则多边形区域内
 */
@SuppressWarnings("all")
public class AreaUtil {

    /**
     * 判断当前位置是否在多边形区域内
     * isIncludEdge为true包括边和顶点 false不包括
     * @param p
     * @param points
     * @param isIncludEdge
     * @return
     */
    public static boolean isInArea(Point p, List<Point> points, Boolean isIncludEdge){
        double p_x =p.getLongitude();
        double p_y =p.getLatitude();
        Point2D.Double point = new Point2D.Double(p_x, p_y);
        List<Point2D.Double> pointList= new ArrayList<>();
        for (Point p2 : points){
            Point2D.Double polygonPoint = new Point2D.Double(p2.getLongitude(),p2.getLatitude());
            pointList.add(polygonPoint);
        }
        return isIncludEdge==true ? isPtInPoly(point,pointList) : isPtInPolyByGeneralPath(point,pointList);
    }

    /**
     * 判断当前位置是否在多边形区域内
     * isIncludEdge为true包括边和顶点 false不包括
     * @param p
     * @param partitionLocation
     * @param isIncludEdge
     * @return
     */
    public static boolean isInArea(Point p, String partitionLocation,Boolean isIncludEdge){
        double p_x =p.getLongitude();
        double p_y =p.getLatitude();
        Point2D.Double point = new Point2D.Double(p_x, p_y);
        List<Point2D.Double> pointList= new ArrayList<>();
        String[] strList = partitionLocation.split(",");
        for (String str : strList){
            String[] points = str.split("_");
            double polygonPoint_x=Double.parseDouble(points[0]);//经度
            double polygonPoint_y=Double.parseDouble(points[1]);//纬度
            Point2D.Double polygonPoint = new Point2D.Double(polygonPoint_x,polygonPoint_y);
            pointList.add(polygonPoint);
        }
        return isIncludEdge==true ? isPtInPoly(point,pointList) : isPtInPolyByGeneralPath(point,pointList);
    }

    /**
     * 判断点是否在多边形内，如果点位于多边形的顶点或边上，也算做点在多边形内，直接返回true
     * @param point 检测点
     * @param pts   多边形的坐标点集合
     * @return
     */
    private static boolean isPtInPoly(Point2D.Double point, List<Point2D.Double> pts){
        int N = pts.size();
        boolean boundOrVertex = true; //如果点位于多边形的顶点或边上，也算做点在多边形内，直接返回true
        int intersectCount = 0;//cross points count of x
        double precision = 2e-10; //浮点类型计算时候与0比较时候的容差
        Point2D.Double p1, p2;//neighbour bound vertices
        Point2D.Double p = point; //当前点
        p1 = pts.get(0);//left vertex
        for(int i = 1; i <= N; ++i){//check all rays
            if(p.equals(p1)){
                return boundOrVertex;//p is an vertex
            }
            p2 = pts.get(i % N);//right vertex
            if(p.x < Math.min(p1.x, p2.x) || p.x > Math.max(p1.x, p2.x)){//ray is outside of our interests
                p1 = p2;
                continue;//next ray left point
            }
            if(p.x > Math.min(p1.x, p2.x) && p.x < Math.max(p1.x, p2.x)){//ray is crossing over by the algorithm (common part of)
                if(p.y <= Math.max(p1.y, p2.y)){//x is before of ray
                    if(p1.x == p2.x && p.y >= Math.min(p1.y, p2.y)){//overlies on a horizontal ray
                        return boundOrVertex;
                    }
                    if(p1.y == p2.y){//ray is vertical
                        if(p1.y == p.y){//overlies on a vertical ray
                            return boundOrVertex;
                        }else{//before ray
                            ++intersectCount;
                        }
                    }else{//cross point on the left side
                        double xinters = (p.x - p1.x) * (p2.y - p1.y) / (p2.x - p1.x) + p1.y;//cross point of y
                        if(Math.abs(p.y - xinters) < precision){//overlies on a ray
                            return boundOrVertex;
                        }
                        if(p.y < xinters){//before ray
                            ++intersectCount;
                        }
                    }
                }
            }else{//special case when ray is crossing through the vertex
                if(p.x == p2.x && p.y <= p2.y){//p crossing over p2
                    Point2D.Double p3 = pts.get((i+1) % N); //next vertex
                    if(p.x >= Math.min(p1.x, p3.x) && p.x <= Math.max(p1.x, p3.x)){//p.x lies between p1.x & p3.x
                        ++intersectCount;
                    }else{
                        intersectCount += 2;
                    }
                }
            }
            p1 = p2;//next ray left point
        }

        if(intersectCount % 2 == 0){//偶数在多边形外
            return false;
        } else { //奇数在多边形内
            return true;
        }
    }

    /**
     * 返回一个点是否在一个多边形区域内
     * 如果点位于多边形的顶点或边上，不算做点在多边形内，返回false
     * @param point
     * @param polygon
     * @return
     */
    private static boolean isPtInPolyByGeneralPath(Point2D.Double point, List<Point2D.Double> polygon) {
        java.awt.geom.GeneralPath p = new java.awt.geom.GeneralPath();
        Point2D.Double first = polygon.get(0);
        p.moveTo(first.x, first.y);
        polygon.remove(0);
        for (Point2D.Double d : polygon) {
            p.lineTo(d.x, d.y);
        }
        p.lineTo(first.x, first.y);
        p.closePath();
        return p.contains(point);
    }

    /**
     * 根据经纬度排序
     * @param args
     */
    public static List<Point> sortPoint(List<Point> points){
        points.sort(new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                //经度从大到小
                int flag = new Double(o2.getLongitude()).compareTo(new Double(o1.getLongitude()));
                if(flag == 0){
                    //纬度从小到大
                    flag = new Double(o1.getLatitude()).compareTo(new Double(o2.getLatitude()));
                }
                return flag;
            }
        });
        return points;
    }

//    public static void main(String[] args) {
//        //上海
//        String a = "121.53959, 30.661087;121.392446, 30.679883;121.349938, 30.680074;121.302675, 30.699653;121.279364, 30.694279;121.278093, 30.707655;121.274774, 30.708233;121.271796, 30.715416;121.278953, 30.728839;121.275555, 30.737214;121.277739, 30.738176;121.274557, 30.740927;121.272866, 30.73928;121.262524, 30.749859;121.238471, 30.762038;121.234748, 30.778157;121.22385, 30.7919;121.212177, 30.792413;121.205871, 30.789566;121.20737, 30.780133;121.203403, 30.779699;121.197214, 30.787328;121.180816, 30.778398;121.175627, 30.781982;121.167321, 30.779006;121.167317, 30.782528;121.158627, 30.784775;121.150377, 30.78518;121.149579, 30.782232;121.129381, 30.784793;121.12368, 30.791504;121.133125, 30.79493;121.134609, 30.815775;121.144219, 30.830594;121.144127, 30.836242;121.140862, 30.834008;121.138324, 30.842662;121.124357, 30.841213;121.130125, 30.853251;121.118255, 30.860113;121.117125, 30.857459;121.108254, 30.856006;121.103157, 30.862877;121.087013, 30.855104;121.072247, 30.854655;121.067309, 30.851566;121.067764, 30.844827;121.053998, 30.836851;121.055738, 30.83189;121.04705, 30.83313;121.043532, 30.825339;121.04989, 30.826531;121.048573, 30.820954;121.044838, 30.820026;121.037273, 30.834908;121.021789, 30.841628;121.010501, 30.831728;121.007821, 30.835425;121.00164, 30.827468;120.996574, 30.829682;120.999209, 30.837688;120.995947, 30.839494;121.017554, 30.859379;121.022381, 30.859112;121.018619, 30.86225;121.022702, 30.865671;121.023369, 30.877247;121.028877, 30.879088;121.025463, 30.888645;121.015024, 30.889154;121.016541, 30.892708;121.013307, 30.894338;120.999392, 30.895633;120.997005, 30.901328;120.999596, 30.90836;121.012253, 30.913926;121.005991, 30.915554;121.007327, 30.943621;121.001687, 30.950534;121.004511, 30.950062;121.004872, 30.954533;121.000289, 30.961369;121.002048, 30.969593;120.999396, 30.974611;121.000779, 30.978491;121.01126, 30.983923;121.007294, 30.986496;121.002381, 31.001361;120.99816, 31.000962;120.997205, 31.020446;120.970423, 31.023435;120.971952, 31.026846;120.967326, 31.028366;120.965271, 31.035911;120.95933, 31.03708;120.955323, 31.035527;120.957152, 31.024635;120.94291, 31.02373;120.946991, 31.015896;120.934662, 31.018386;120.920109, 31.016859;120.917545, 31.022735;120.908959, 31.023034;120.910537, 31.039574;120.903484, 31.05073;120.907108, 31.079606;120.911357, 31.086222;120.898726, 31.09797;120.884088, 31.101765;120.882738, 31.105976;120.868669, 31.10622;120.863182, 31.112147;120.874931, 31.122764;120.878476, 31.132625;120.888915, 31.140995;120.919751, 31.141646;120.937006, 31.147251;120.988733, 31.138189;121.024523, 31.13974;121.032271, 31.148287;121.046521, 31.143282;121.050532, 31.160893;121.054786, 31.160209;121.055283, 31.155957;121.0679, 31.158786;121.070542, 31.154154;121.074051, 31.154345;121.083068, 31.17091;121.070188, 31.237258;121.066495, 31.242302;121.069371, 31.247499;121.068155, 31.274234;121.088097, 31.277361;121.092968, 31.297776;121.109726, 31.28041;121.12015, 31.290179;121.126448, 31.291929;121.139792, 31.284158;121.150057, 31.283336;121.149642, 31.280708;121.160665, 31.282393;121.167809, 31.289819;121.157847, 31.297627;121.15807, 31.302064;121.154357, 31.303861;121.157348, 31.304705;121.150358, 31.315341;121.146229, 31.308622;121.136514, 31.308275;121.133168, 31.324392;121.139881, 31.332003;121.136917, 31.348043;121.120769, 31.348836;121.12279, 31.356402;121.113986, 31.357121;121.11232, 31.36102;121.113719, 31.371457;121.120575, 31.369503;121.127355, 31.374488;121.122903, 31.380573;121.139747, 31.383344;121.145003, 31.385718;121.144501, 31.388873;121.153817, 31.391196;121.155452, 31.402869;121.160545, 31.404705;121.157162, 31.409917;121.167663, 31.416204;121.159766, 31.420481;121.156316, 31.426616;121.173689, 31.436134;121.171525, 31.438992;121.158115, 31.441551;121.158229, 31.44649;121.171653, 31.454116;121.180681, 31.453703;121.193387, 31.458683;121.197744, 31.470072;121.210028, 31.475673;121.218512, 31.48376;121.231171, 31.480707;121.237992, 31.483813;121.238099, 31.489422;121.24409, 31.496279;121.249399, 31.496412;121.25639, 31.482239;121.265154, 31.481787;121.276785, 31.491387;121.282661, 31.490087;121.299797, 31.497158;121.303211, 31.49522;121.314783, 31.51299;121.318276, 31.51274;121.319597, 31.507791;121.326519, 31.511784;121.326987, 31.506147;121.337409, 31.511543;121.318631, 31.534163;121.281191, 31.591225;121.253944, 31.623826;121.217127, 31.660286;121.162867, 31.696228;121.148773, 31.724389;121.124911, 31.754476;121.129562, 31.758539;121.130694, 31.768459;121.138591, 31.780844;121.175277, 31.815393;121.200754, 31.834322;121.216167, 31.84436;121.279957, 31.872669;121.303265, 31.879588;121.323731, 31.879719;121.354793, 31.868764;121.374835, 31.856779;121.407267, 31.827291;121.431214, 31.810944;121.528308, 31.769356;121.578976, 31.741839;121.671799, 31.701375;121.848616, 31.643655;121.922973, 31.62868;122.013288, 31.625913;122.006556, 31.455078;122.026264, 31.237448;122.029708, 31.144107;121.993597, 31.048175;122.005321, 30.819252;122.023575, 30.823739;122.007096, 30.776183;121.978924, 30.766885;121.920869, 30.740022;121.881569, 30.729712;121.795492, 30.710728;121.610755, 30.679711;121.53959, 30.661087";
//        String[] strings = a.split(";");
//        List<Point> points = new ArrayList<>();
//        for (String s:strings) {
//            Point p = new Point();
//            p.setLongitude(Double.parseDouble(s.split(",")[0].replace(" ","")));
//            p.setLatitude(Double.parseDouble(s.split(",")[1].replace(" ","")));
//            points.add(p);
//        }
//        String s = JsonUtil.writeValueAsString(points);
//        String replace = s.replace("longitude", "lng");
//        String replace2 = replace.replace("latitude", "lat");
//        System.out.println(replace2);
//        long start = System.currentTimeMillis();
//        for (Point p:points) {
//            boolean b = isInArea(p, points, true);
//            System.out.println(b);
//        }
//        System.out.println("--------------");
//        boolean b = isInArea(new Point(120.564756,31.211817), points, true);
//        System.out.println(b);
//        long end = System.currentTimeMillis();
//    }
}
