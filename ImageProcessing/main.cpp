#include <iostream>
#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <vector>

using namespace std;
using namespace cv;

Mat deskew(Mat img, double angle){
    cv::bitwise_not(img,img);

    std::vector<cv::Point> points;
    cv::Mat_<uchar>::iterator it = img.begin<uchar>();
    cv::Mat_<uchar>::iterator end = img.end<uchar>();
    for(;it != end;it++){
        if(*it){
            points.push_back(it.pos());
        }
    }


    // rotated box
    cv::RotatedRect box = cv::minAreaRect(cv::Mat(points));

    cv::Mat rot_mat = cv::getRotationMatrix2D(box.center, angle, 1);
    cv::Mat rotated;
    cv::warpAffine(img, rotated, rot_mat, img.size(), cv::INTER_CUBIC);

    cv::Size box_size = box.size;
    if (box.angle < -45.){
        std::swap(box_size.width, box_size.height);
    }
    cv::Mat cropped;
    cv::getRectSubPix(rotated, box_size, box.center, cropped);
    // cropped mat will contain the text with minimal margins

//    imshow("deskewed", rotated);
//    waitKey(0);

    return rotated;
}

double compute_skew(Mat img) {

    Mat src = img;
//    bitwise_not(img, src);
    Size size = img.size();


    std::vector<cv::Vec4i> lines;
    cv::HoughLinesP(src, lines, 1, CV_PI/180, 60, 30, 40);

//    cv::Mat disp_lines(size, CV_8UC1, cv::Scalar(0, 0, 0));
    double angle = 0.;
    unsigned nb_lines = lines.size();
    for (unsigned i = 0; i < nb_lines; ++i)
    {
//        cv::line(disp_lines, cv::Point(lines[i][0], lines[i][1]),
//                 cv::Point(lines[i][2], lines[i][3]), cv::Scalar(255, 0 ,0));
        double ang = atan2((double)lines[i][3] - lines[i][1],
                       (double)lines[i][2] - lines[i][0]);
        double degree = ang * 180 / CV_PI;
        if(degree > 45 || degree < -45) {
            continue;
        }
//        cout << degree << "\n";
        angle += ang;

    }
    angle /= nb_lines; // mean angle, in radians.

    std::cout <<" Skew angle: " << angle * 180 / CV_PI << std::endl;

//    cv::imshow("Lines", disp_lines);
//    cv::waitKey(0);

    return angle * 180 / CV_PI;
}

int main(int argc, char** argv)
{

    if( argc != 3)
    {
        cout <<" Usage: ProcessImage input_image output_image" << endl;
        return -1;
    }

    Mat image;
    image = imread(argv[1], CV_LOAD_IMAGE_COLOR);   // Read the file

    if(! image.data )                              // Check for invalid input
    {
        cout <<  "Could not open or find the image" << std::endl ;
        return -1;
    }


    Mat gray_image;
    cvtColor(image, gray_image, CV_RGB2GRAY);
    Mat blurred;
    GaussianBlur(gray_image, blurred, Size(1,1), 10);

    int dilation_size = 1;
    Mat element = getStructuringElement( MORPH_RECT,
                                         Size( 2*dilation_size + 1, 2*dilation_size+1 ),
                                         Point( dilation_size, dilation_size ) );

    // Apply the dilation operation

   /* Mat inv = Scalar::all(255) - gray_image;
    Mat dilated;
    dilate( inv, dilated, element );
    imshow("dilated", dilated);

    waitKey(0)*/;

    Mat edges;
    Canny( blurred, edges, 50, 200, 3 );

    double angle = compute_skew(edges);

//    namedWindow( "Display window", WINDOW_AUTOSIZE );
//    imshow( "Display window", edges);
//    waitKey(0);
    Mat deskewed = deskew(blurred, angle);

//    imshow("fixed", deskewed);
//    waitKey(0);

    erode(deskewed, deskewed, element);

    dilation_size = 1;
    element = getStructuringElement( MORPH_RECT,
                                         Size( 2*dilation_size + 1, 2*dilation_size+1 ),
                                         Point( dilation_size, dilation_size ) );

    dilate(deskewed, deskewed, element);

    Mat out;
//    adaptiveThreshold(deskewed, out, 255, ADAPTIVE_THRESH_MEAN_C,THRESH_BINARY_INV, 5, 0);
    threshold(deskewed, out, 122, 255, THRESH_BINARY_INV);

    imwrite(argv[2], out);

    return 0;

}
