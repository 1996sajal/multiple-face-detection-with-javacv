/*
 *   Created by SAJAL TYAGI
 *   31-12-2015
 *   12:27 IST
 */
import com.googlecode.javacpp.Loader;
import static com.googlecode.javacv.cpp.opencv_core.CV_AA;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import static com.googlecode.javacv.cpp.opencv_core.cvClearMemStorage;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvFont;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSeqElem;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSize;
import static com.googlecode.javacv.cpp.opencv_core.cvLoad;
import static com.googlecode.javacv.cpp.opencv_core.cvPoint;
import static com.googlecode.javacv.cpp.opencv_core.cvPutText;
import static com.googlecode.javacv.cpp.opencv_core.cvRectangle;
import static com.googlecode.javacv.cpp.opencv_core.cvReleaseImage;
import static com.googlecode.javacv.cpp.opencv_highgui.CV_CAP_ANY;
import com.googlecode.javacv.cpp.opencv_highgui.CvCapture;
import static com.googlecode.javacv.cpp.opencv_highgui.cvCreateCameraCapture;
import static com.googlecode.javacv.cpp.opencv_highgui.cvQueryFrame;
import static com.googlecode.javacv.cpp.opencv_highgui.cvShowImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvWaitKey;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_INTER_LINEAR;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvEqualizeHist;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvResize;
import com.googlecode.javacv.cpp.opencv_objdetect;
import static com.googlecode.javacv.cpp.opencv_objdetect.CV_HAAR_DO_CANNY_PRUNING;
import com.googlecode.javacv.cpp.opencv_objdetect.CvHaarClassifierCascade;
import static com.googlecode.javacv.cpp.opencv_objdetect.cvHaarDetectObjects;

public class MultipleFaces {

    public static void main(String[] args) {

        Loader.load(opencv_objdetect.class);
        CvCapture capture = cvCreateCameraCapture(CV_CAP_ANY);
        IplImage org = null, gray = null, small = null;
        int scale = 2;
        CvMemStorage cms = null;
        CvSeq cs;
        int total = 0;
        int bal = 1;
        while (bal == 1) {
            org = cvQueryFrame(capture);
            if (org == null) {
                break;
            }
            gray = cvCreateImage(cvGetSize(org), 8, 1);
            cvCvtColor(org, gray, CV_BGR2GRAY);
            small = IplImage.create(gray.width() / scale, gray.height() / scale, 8, 1);
            cvResize(gray, small, CV_INTER_LINEAR);
            cvEqualizeHist(small, small);
            cms = CvMemStorage.create();
            CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(
                    cvLoad("E:\\Downloads\\Java projects\\JavaCV\\src\\com\\facedetect\\haarcascade_frontalface_alt.xml"));
            cs = cvHaarDetectObjects(small, cascade, cms, 1.1, 3,
                    CV_HAAR_DO_CANNY_PRUNING);
            total = cs.total();
            if (total > 0) {
                for (int i = 0; i < total; i++) {
                    CvRect r = new CvRect(cvGetSeqElem(cs, i));
                    cvRectangle(org, cvPoint(r.x() * scale, r.y() * scale),
                            cvPoint((r.x() + r.width()) * scale, (r.y() + r.height()) * scale),
                            CvScalar.YELLOW, 6, CV_AA, 0);
                }

            }
            if ((char) cvWaitKey(1) == 'q') {
                break;
            }
            cvPutText(org, "#SAJAL", cvPoint(40, 40), cvFont(scale, 5), new CvScalar(0, 0, 255, 1));
            cvShowImage("Video", org);
        }
        cvReleaseImage(org);
        cvReleaseImage(gray);
        cvReleaseImage(small);
        cvClearMemStorage(cms);

    }

}
