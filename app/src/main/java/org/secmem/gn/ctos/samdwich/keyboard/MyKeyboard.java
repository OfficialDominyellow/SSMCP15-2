package org.secmem.gn.ctos.samdwich.keyboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import org.secmem.gn.ctos.samdwich.R;
import org.secmem.gn.ctos.samdwich.global.GlobalVariable;
import org.secmem.gn.ctos.samdwich.global.MyMath;


/**
 * Created by 김희중 on 2016-01-08.
 */
public class MyKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener{

    private final String TAG = "MyKeyboard";
    private final String COUNT_TAG = "SamdwichCount";

    private int mTouchCount = 0;
    private int mDeleteCount = 0;

    private static final int HAN_MODE = 0;
    private static final int ENG_MODE = 1;
    private static final int NUM_MODE = 2;
    private static final int SPECIAL_MODE = 3;

    private int inputKeyboardMode = HAN_MODE;

    private MyKeyboardView kv;

    private Keyboard keyboard;
    private Keyboard keyboardLeft;
    private Keyboard keyboardRight;
    private Keyboard keyboardEng;
    private Keyboard keyboardNum;

    private boolean caps = false;

    private double xPositionStart;
    private double yPositionStart;

    private double xPositionEnd;
    private double yPositionEnd;

    private int downKeycode;
    private float mDownKeyRotate = 0; //누른 키 라디안 값

    private int prevTouchKeyCode;
    private int currTouchKeyCode;
    private static Intent mService;
    private int handValue;
    private boolean handFlag=true;

    private class Hangul {
        private static final int INPUT_MODE_CHO = 0;
        private static final int INPUT_MODE_JUNG = 1;
        private static final int INPUT_MODE_JONG = 2;

        public String chosung = "";
        public String jungsung = "";
        public String jongsung = "";
        public int step = INPUT_MODE_CHO; //0 : 초성 입랙대기, 1 : 중성 입력대기, 2 : 종성 입력대기

        private boolean writingJongsungFlag = false; //전글짜의 받침이 다음글짜 초성으로 넘어올 수도 있을 때 true
        private boolean dotUsedFlag = false; //방금 천지인 . 썼어
        private boolean doubledFlag = false; //방금 천지인 .. 썼어
        private int mDelCount = 0;
        private String mLeftExtra="";

        // ㄱ  ㄲ  ㄴ  ㄷ  ㄸ  ㄹ  ㅁ  ㅂ  ㅃ  ㅅ  ㅆ  ㅇ  ㅈ  ㅉ  ㅊ  ㅋ  ㅌ  ㅍ  ㅎ
        private final int[] PREF_CHO = {
                12593, 12594, 12596, 12599, 12600, 12601, 12609, 12610,
                12611, 12613, 12614, 12615, 12616, 12617, 12618, 12619,
                12620, 12621, 12622};
        // ㅏ  ㅐ  ㅑ  ㅒ  ㅓ  ㅔ  ㅕ  ㅖ  ㅗ  ㅘ  ㅙ  ㅚ  ㅛ  ㅜ  ㅝ  ㅞ  ㅟ  ㅠ  ㅡ  ㅢ  ㅣ
        private final int[] PREF_JUNG = {
                12623, 12624, 12625, 12626, 12627, 12628, 12629, 12630,
                12631, 12632, 12633, 12634, 12635, 12636, 12637, 12638,
                12639, 12640, 12641, 12642, 12643};
        // ㄱ  ㄲ  ㄳ  ㄴ  ㄵ  ㄶ  ㄷ  ㄹ  ㄺ  ㄻ  ㄼ  ㄽ  ㄾ  ㄿ  ㅀ  ㅁ  ㅂ  ㅄ  ㅅ  ㅆ  ㅇ  ㅈ  ㅊ  ㅋ  ㅌ  ㅍ  ㅎ
        private final int[] PREF_JONG = {
                12593, 12594, 12595, 12596, 12597, 12598, 12599, 12601,
                12602, 12603, 12604, 12605, 12606, 12607, 12608, 12609,
                12610, 12612, 12613, 12614, 12615, 12616, 12618, 12619,
                12620, 12621, 12622};
        //ㅡ, ㅣ, .
        private final int[] PREF_CHUNJIIN = {
                12641, 12643, 12685
        };

        public void init() {
            this.chosung = "";
            this.jungsung = "";
            this.jongsung = "";
            this.step = INPUT_MODE_CHO;
            this.writingJongsungFlag = false;
            this.dotUsedFlag = false;
            this.doubledFlag = false;
            this.mDelCount = 0;
        }

        public Hangul(){
            init();
        }

        private boolean isInSet(int[] hanSet, int primaryCode){
            for(int codeOfSet : hanSet){
                if(codeOfSet == primaryCode){
                    return true;
                }
            }
            return false;
        }

        private boolean isDoubleJa(String ja){
            if(ja.equals("ㄳ") || ja.equals("ㄵ") || ja.equals("ㄶ") || ja.equals("ㄺ") || ja.equals("ㄻ") || ja.equals("ㄼ") || ja.equals("ㄽ") || ja.equals("ㄾ") || ja.equals("ㄿ") || ja.equals("ㅀ") || ja.equals("ㅄ")){
                return true;
            }
            return false;
        }
        private int getDoubleLeftCode(String ja){
            if(ja.equals("ㄳ")){
                return 12593;
            }
            else if(ja.equals("ㄵ") || ja.equals("ㄶ")){
                return 12596;
            }
            else if(ja.equals("ㄺ") || ja.equals("ㄻ") || ja.equals("ㄼ") || ja.equals("ㄽ") || ja.equals("ㄾ") || ja.equals("ㄿ") || ja.equals("ㅀ")){
                return 12601;
            }
            else if(ja.equals("ㅄ")){
                return 12610;
            }
            return -1;
        }
        private int getDoubleRightCode(String ja){
            if(ja.equals("ㄳ") || ja.equals("ㅄ") || ja.equals("ㄽ")){
                return 12613;
            }
            else if(ja.equals("ㄶ") || ja.equals("ㅀ")){
                return 12622;
            }
            else if(ja.equals("ㄺ")){
                return 12593;
            }
            else if(ja.equals("ㄵ")){
                return 12616;
            }
            else if(ja.equals("ㄻ")){
                return 12609;
            }
            else if(ja.equals("ㄼ")){
                return 12610;
            }
            else if(ja.equals("ㄾ")){
                return 12620;
            }
            else if(ja.equals("ㄿ")){
                return 12621;
            }

            return -1;
        }
        private int getDoublePairCode(String s1, String s2){
            if(s1.equals("ㄱ")){
                if(s2.equals("ㅅ")){
                    return 12595;
                }
            }
            else if(s1.equals("ㄴ")){
                if(s2.equals("ㅈ")){
                    return 12597;
                }
                else if(s2.equals("ㅎ")){
                    return 12598;
                }
            }
            else if(s1.equals("ㄹ")){
                if(s2.equals("ㄱ")){
                    return 12602;
                }
                else if(s2.equals("ㅁ")){
                    return 12603;
                }
                else if(s2.equals("ㅂ")){
                    return 12604;
                }
                else if(s2.equals("ㅅ")){
                    return 12605;
                }
                else if(s2.equals("ㅌ")){
                    return 12606;
                }
                else if(s2.equals("ㅠ")){
                    return 12607;
                }
                else if(s2.equals("ㅎ")){
                    return 12608;
                }
            }
            else if(s1.equals("ㅂ")){
                if(s2.equals("ㅅ")){
                    return 12612;
                }
            }
            return -1;
        }

        boolean isMoSet(String prevMo, int pc){
            switch(pc){
                //ㅣ
                case 12641:
                    return prevMo.equals("") || (prevMo.equals("ㆍ") && this.dotUsedFlag) || (prevMo.equals("ㆍ") && this.doubledFlag);
                //ㅡ
                case 12643:
                    return prevMo.equals("") || (prevMo.equals("ㆍ") && this.dotUsedFlag) || (prevMo.equals("ㆍ") && this.doubledFlag || prevMo.equals("ㅏ") || prevMo.equals("ㅑ") || prevMo.equals("ㅓ") || prevMo.equals("ㅕ") || prevMo.equals("ㅗ") || prevMo.equals("ㅜ") || prevMo.equals("ㅠ") || prevMo.equals("ㅡ"));
                //.
                case 12685:
                    return prevMo.equals("") || (prevMo.equals("ㆍ") && this.dotUsedFlag) || prevMo.equals("ㅣ") || prevMo.equals("ㅏ") || prevMo.equals("ㅚ") || prevMo.equals("ㅡ") || prevMo.equals("ㅜ");
                default:

            }
            return false;
        }


        private void makeHangul(int primaryCode) {
            switch(this.step){
                //초성입력
                case INPUT_MODE_CHO:
                    //자음이 왔어
                    if(isInSet(PREF_CHO, primaryCode)){
                        //첫 자음이야
                        if(this.chosung.equals("")){
                            //그냥 입력해
                            this.chosung = (char) primaryCode + "";
                            mDelCount = 0;
                        }
                        //이미 입력된 자음이 있어
                        else {
                            int choCode = getDoublePairCode(this.chosung, (char)primaryCode+"");

                            //현재 있는 자음과 쌍자음이 성립되면
                            if(choCode != -1){
                                this.chosung = (char)choCode + "";
                                mDelCount = 1;
                            }
                            //쌍자음 성립 안되면 그냥 입력해
                            else {
                                this.chosung = (char) primaryCode + "";
                                mDelCount = 0;
                            }
                        }
                    }
                    //천지인 모음 입력이 왔어
                    else if (isInSet(PREF_CHUNJIIN, primaryCode)){
                        step = INPUT_MODE_JUNG;
                        //아래아가 왔을 때
                        if(primaryCode == 12685) {
                            Log.i(TAG, ".? : " + (char)primaryCode+"");
                            //일단 아래아를 그냥 찍어
                            mDelCount = 0;
                            dotUsedFlag = true;
                            this.jungsung = (char)primaryCode + "";

                        }
                        //ㅡ ㅣ
                        else {
                            //초성이 비었어
                            if(this.chosung.equals("")){
                                Log.i(TAG, "chosung empty ㅡ ㅣ ");
                                mDelCount=0;
                                this.jungsung = (char)primaryCode + "";
                            }
                            //초성이 있어
                            else {
                                //앞이 쌍자음이고 모음이 오면 쪼개야해 ㄳㅣ -> ㄱ시
                                if (isDoubleJa(this.chosung)) {
                                    mDelCount = 1;
                                    mLeftExtra = (char) getDoubleLeftCode(this.chosung) + "";
                                    this.chosung = (char) getDoubleRightCode(this.chosung) + "";
                                }
                                //앞이 그냥 자음이야
                                else {
                                    mDelCount = 1;
                                }
                                this.jungsung = (char) primaryCode + "";
                            }
                        }
                    }
                    break;
                case INPUT_MODE_JUNG:
                    //자음이 왔어 -> 받침이야, 혹은 dot 2개 이후 의미없는 자음이 올수도
                    if(isInSet(PREF_CHO, primaryCode)){
                        //최근이 dot들이면 받침이 안되고 따로 빠져야해
                        if((dotUsedFlag || doubledFlag) && this.jungsung.equals("ㆍ")) {
                            init();
                            this.chosung = (char)primaryCode + "";
                        }
                        else {
                            //다좋은데 ㄸ, ㅃ, ㅉ 받침 안돼
                            if (primaryCode == 12600 || primaryCode == 12611 || primaryCode == 12617) {
                                init();
                                this.chosung = (char)primaryCode + "";
                            }
                            //나머지는 받침 찍고 종성입력단계로
                            else {
                                //하지만 초성이 없다면 얘기가 달라지지
                                if (this.chosung.equals("")) {
                                    init();
                                    this.chosung = (char)primaryCode + "";
                                } else {
                                    step = INPUT_MODE_JONG;
                                    mDelCount = 1;
                                    this.jongsung = (char) primaryCode + "";
                                }
                            }
                        }
                    }
                    //천지인 모음 계속 입력하네
                    else if(isInSet(PREF_CHUNJIIN, primaryCode)) {
                        //받침에서 넘어올꺼야
                        if(writingJongsungFlag){
                            if(dotUsedFlag){
                                dotUsedFlag = false;

                                //점 또찍어 아래아 2개 모음
                                if (primaryCode == 12685) {
                                    doubledFlag = true;
                                }
                                //모음 완성
                                else {
                                    mDelCount = 2;
                                    writingJongsungFlag = false;
                                    //받침이 복자음이면 하나 남기고 오른쪽 자음만 빼와야해
                                    if (isDoubleJa(this.jongsung)) {
                                        String jongLeftStr = (char) getDoubleLeftCode(this.jongsung) + "";
                                        String jongRightStr = (char) getDoubleRightCode(this.jongsung) + "";
                                        this.jongsung = jongLeftStr;
                                        mLeftExtra = (char) getUnicode() + "";

                                        this.chosung = jongRightStr;
                                        this.jongsung = "";
                                        if (primaryCode == 12641) {
                                            this.jungsung = "ㅗ";
                                        } else if (primaryCode == 12643) {
                                            this.jungsung = "ㅓ";
                                        }
                                    }
                                    //그냥 자음이면 받침 없애고 자음 빼와야해
                                    else {
                                        String jongStr = this.jongsung;
                                        this.jongsung = "";
                                        mLeftExtra = (char) getUnicode() + "";

                                        this.chosung = jongStr;
                                        if (primaryCode == 12641) {
                                            this.jungsung = "ㅗ";
                                        } else if (primaryCode == 12643) {
                                            this.jungsung = "ㅓ";
                                        }
                                    }
                                }
                            }
                            else if(doubledFlag){
                                doubledFlag = false;
                                //점 또찍어 아래아 2개에 dot 추가되면 그냥 따로야
                                if(primaryCode == 12685) {
                                    init();
                                    step = INPUT_MODE_JUNG;
                                    this.jungsung = (char) primaryCode + "";
                                    dotUsedFlag = true;
                                }
                                //모음 완성
                                else{
                                    mDelCount=3;
                                    writingJongsungFlag = false;
                                    //받침이 복자음이면 하나 남기고 오른쪽 자음만 빼와야해
                                    if(isDoubleJa(this.jongsung)){
                                        String jongLeftStr = (char)getDoubleLeftCode(this.jongsung) + "";
                                        String jongRightStr = (char) getDoubleRightCode(this.jongsung) + "";
                                        this.jongsung = jongLeftStr;
                                        mLeftExtra = (char)getUnicode() + "";

                                        this.chosung =  jongRightStr;
                                        this.jongsung = "";
                                        if(primaryCode == 12641){
                                            this.jungsung = "ㅛ";
                                        }
                                        else if(primaryCode == 12643){
                                            this.jungsung = "ㅕ";
                                        }
                                    }
                                    //그냥 자음이면 받침 없애고 자음 빼와야해
                                    else{
                                        String jongStr = this.jongsung;
                                        this.jongsung = "";
                                        mLeftExtra = (char)getUnicode() + "";

                                        this.chosung = jongStr;
                                        if(primaryCode == 12641){
                                            this.jungsung = "ㅛ";
                                        }
                                        else if(primaryCode == 12643){
                                            this.jungsung = "ㅕ";
                                        }
                                    }
                                    writingJongsungFlag = false;
                                }
                            }
                        }
                        //받침에서 넘어오는 그딴거 없어 최근이 모음
                        else {
                            //모음 세트야
                            //최근에 천지인 . 찍었어
                            //. ㅏ ㅜ // ㅘ
                            if (dotUsedFlag) {
                                //다시한번 아래아
                                if (primaryCode == 12685) {
                                    dotUsedFlag = false;
                                    doubledFlag = true;
                                    if (this.jungsung.equals("ㆍ")) {
                                        dotUsedFlag = false;
                                        doubledFlag = true;
                                    }
                                    //ㅡ  ㅣ
                                    else {
                                        mDelCount = 1;
                                        if (this.jungsung.equals("ㅏ")) {
                                            this.jungsung = "ㅑ";
                                        } else if (this.jungsung.equals("ㅜ")) {
                                            this.jungsung = "ㅠ";
                                        }
                                    }
                                }
                                //모음완성
                                else {
                                    dotUsedFlag = false;
                                    //심지어 앞에 온 자음이 쌍자음 ㄳ . -> ㄱ서
                                    if (isDoubleJa(this.chosung)) {
                                    /*
                                    //의미없는 쌍자음 합치기
                                    if(writingDoubleFlag) {
                                        mDelCount = 2;
                                        int chosungLeftCode = getDoubleLeftCode(this.chosung);
                                        int chosungRightCode = getDoubleRightCode(this.chosung);
                                        mLeftExtra = (char)chosungLeftCode + "";

                                        this.chosung = (char)chosungRightCode + "";

                                        if (this.jungsung.equals("ㅏ")) {
                                            this.jungsung = "ㅑ";
                                        } else if (this.jungsung.equals("ㅜ")) {
                                            this.jungsung = "ㅠ";
                                        }

                                        writingDoubleFlag = false;
                                    }
                                    */
                                        Log.i(TAG, "double cho + dot + mo");
                                        mDelCount = 2;
                                        mLeftExtra = (char) getDoubleLeftCode(this.chosung) + "";
                                        this.chosung = (char) getDoubleRightCode(this.chosung) + "";
                                        //ㅡ
                                        if (primaryCode == 12641) {
                                            this.jungsung = "ㅗ";
                                        }
                                        //ㅣ
                                        else if (primaryCode == 12643) {
                                            this.jungsung = "ㅓ";
                                        }

                                        Log.i(TAG, "mleftExtra : " + mLeftExtra + " cho : " + this.chosung + " jung : " + this.jungsung);

                                    }
                                    //앞에 그냥 자음 ㅈ. -> 저
                                    else {
                                        mDelCount = 2;
                                        //초성이 없으면 하나만 지워
                                        if (this.chosung.equals("")) {
                                            mDelCount = 1;
                                        }
                                        //+ㅡ -> ㅗ
                                        if (primaryCode == 12641) {
                                            //ㅗ
                                            if (this.jungsung.equals("ㆍ")) {
                                                //초성이 없으면 하나만 지워
                                                if (this.chosung.equals("")) {
                                                    mDelCount = 1;
                                                }
                                                this.jungsung = "ㅗ";
                                            }
                                            //조합이 안맞아
                                            else {
                                                init();
                                                this.jungsung = (char)primaryCode +"";
                                                this.step = INPUT_MODE_JUNG;
                                            }
                                        }
                                        //+ㅣ -> ㅓ ㅐ ㅟ ㅙ
                                        else if (primaryCode == 12643) {
                                            if (this.jungsung.equals("ㆍ")) {
                                                mDelCount = 2;
                                                //초성이 없으면 하나만 지워
                                                if (this.chosung.equals("")) {
                                                    mDelCount = 1;
                                                }
                                                this.jungsung = "ㅓ";
                                            } else if (this.jungsung.equals("ㅏ")) {
                                                mDelCount = 1;
                                                this.jungsung = "ㅐ";
                                            } else if (this.jungsung.equals("ㅜ")) {
                                                mDelCount = 1;
                                                this.jungsung = "ㅟ";
                                            } else if (this.jungsung.equals("ㅘ")) {
                                                mDelCount = 1;
                                                this.jungsung = "ㅙ";
                                            }
                                            //조합이 안맞아
                                            else {
                                                init();
                                                this.jungsung = (char) primaryCode+"";
                                                this.step = INPUT_MODE_JUNG;
                                                dotUsedFlag = true;
                                            }
                                        }
                                    }
                                }
                            }
                            //최근에 천지인 .. 찍었어
                            //.. ㅑ ㅠ
                            else if (doubledFlag) {
                                //ㅛ ㅕ
                                doubledFlag = false;
                                //아래아 또 찍었어 글씨 따로 빠져
                                if (primaryCode == 12685) {
                                    init();
                                    this.jungsung = (char)primaryCode + "";
                                    step = INPUT_MODE_JUNG;
                                    dotUsedFlag = true;
                                }
                                //모음완성
                                else {
                                    dotUsedFlag = false;

                                    //심지어 앞에 온 자음이 쌍자음
                                    if (isDoubleJa(this.chosung)) {
                                        mDelCount = 3;
                                        mLeftExtra = (char) getDoubleLeftCode(this.chosung) + "";
                                        this.chosung = (char) getDoubleRightCode(this.chosung) + "";

                                        //ㅡ
                                        if (primaryCode == 12641) {
                                            this.jungsung = "ㅛ";
                                        }
                                        //ㅣ
                                        else if (primaryCode == 12643) {
                                            this.jungsung = "ㅕ";
                                        }
                                    }

                                    //그냥 자음
                                    else {
                                        //+ㅡ -> ㅛ
                                        if (primaryCode == 12641) {
                                            //ㅛ
                                            if (this.jungsung.equals("ㆍ")) {
                                                mDelCount = 3;
                                                //초성이 없으면 두개 지워
                                                if (this.chosung.equals("")) {
                                                    mDelCount = 2;
                                                }
                                                this.jungsung = "ㅛ";
                                            }
                                            //조합이 안나와
                                            else {
                                                init();
                                                this.jungsung = (char)primaryCode + "";
                                                step = INPUT_MODE_JUNG;
                                            }
                                        }
                                        //+ㅣ -> ㅕ ㅒ ㅝ
                                        else if (primaryCode == 12643) {

                                            if (this.jungsung.equals("ㆍ")) {
                                                mDelCount = 3;
                                                //초성이 없으면 두개만 지워
                                                if (this.chosung.equals("")) {
                                                    mDelCount = 2;
                                                }
                                                this.jungsung = "ㅕ";
                                            } else if (this.jungsung.equals("ㅑ")) {
                                                mDelCount = 1;
                                                this.jungsung = "ㅒ";
                                            } else if (this.jungsung.equals("ㅠ")) {
                                                mDelCount = 1;
                                                this.jungsung = "ㅝ";
                                            }
                                        }
                                    }
                                }
                            }
                            //최근에 아래아 안찍었어
                            //ㅗ ㅓ ㅡ ㅣ ㅛ ㅕ ㅝ // ㅐ ㅒ ㅔ ㅖ ㅚ ㅙ ㅟ ㅞ ㅢ
                            else {
                                //아래아가 왔어
                                mDelCount = 1;
                                if (primaryCode == 12685) {
                                    dotUsedFlag = true;
                                    //아래아 찍으면 모음이 깨져버려 그게 아닌애들
                                    if (this.jungsung.equals("ㅡ")) {
                                        this.jungsung = "ㅜ";
                                    } else if (this.jungsung.equals("ㅣ")) {
                                        this.jungsung = "ㅏ";
                                    } else if (this.jungsung.equals("ㅚ")) {
                                        this.jungsung = "ㅘ";
                                    }
                                    //글씨 따로 빠져
                                    else {
                                        init();
                                        this.jungsung = (char)primaryCode + "";
                                        step = INPUT_MODE_JUNG;
                                        dotUsedFlag = true;
                                    }
                                }
                                // ㅡ ㅣ
                                else {
                                    // 최근에 아래아 없이 ㅡ 못와 -> 글씨 따로 빠져
                                    if (primaryCode == 12641) {
                                        init();
                                        this.jungsung = (char)primaryCode + "";
                                        step = INPUT_MODE_JUNG;
                                    }
                                    // ㅣ
                                    else if (primaryCode == 12643) {
                                        mDelCount = 1;
                                        if (this.jungsung.equals("ㅗ")) {
                                            this.jungsung = "ㅚ";
                                        } else if (this.jungsung.equals("ㅓ")) {
                                            this.jungsung = "ㅔ";
                                        } else if (this.jungsung.equals("ㅕ")) {
                                            this.jungsung = "ㅖ";
                                        } else if (this.jungsung.equals("ㅝ")) {
                                            this.jungsung = "ㅞ";
                                        } else if (this.jungsung.equals("ㅡ")) {
                                            this.jungsung = "ㅢ";
                                        }
                                        //최근에 아래아 없는 모음중에 이 외의 것은 l와 조합 안돼 -> 글씨 따로 빠져

                                        else {
                                            init();
                                            this.jungsung = (char)primaryCode + "";
                                            step = INPUT_MODE_JUNG;
                                        }
                                    }
                                }
                            }

                        }
                    }

                    break;
                case INPUT_MODE_JONG:
                    //받침 입력
                    //자음 왔어
                    if(isInSet(PREF_CHO, primaryCode)) {
                        int doubleCode = getDoublePairCode(this.jongsung, (char)primaryCode+"");
                        //지금 들어온 자음이 기존 받침이랑 복자음이야
                        if(doubleCode != -1){
                            mDelCount = 1;
                            this.jongsung = (char)doubleCode+"";
                        }
                        //기존에 있던 받침이랑 복자음이 안돼
                        else {
                            init();
                            this.chosung = (char)primaryCode + "";
                        }
                    }
                    //천지인 모음 왔어
                    else if(isInSet(PREF_CHUNJIIN, primaryCode)){
                        //점왓다 -> 받침 + 아래아모음
                        if(primaryCode == 12685){
                            Log.i(TAG, "jongsung + dot");
                            mDelCount = 0;
                            dotUsedFlag = true;
                            writingJongsungFlag = true;
                            step = INPUT_MODE_JUNG;
                        }
                        else {
                            this.dotUsedFlag = this.doubledFlag = false;
                            //받침이 복자음이면 나눠야해
                            if (isDoubleJa(this.jongsung)) {
                                mDelCount = 1;
                                int jongsungLeftCode = getDoubleLeftCode(this.jongsung);
                                int jongsungRightCode = getDoubleRightCode(this.jongsung);
                                this.jongsung = (char)jongsungLeftCode+"";
                                mLeftExtra = (char)getUnicode()+"";

                                this.chosung = (char) jongsungRightCode + "";
                                this.jungsung = (char) primaryCode + "";
                                this.jongsung = "";
                                step = INPUT_MODE_JUNG;
                            }
                            //받침 그냥 자음이야 나눠
                            else {
                                mDelCount = 1;
                                String jongsungPrev = this.jongsung;
                                this.jongsung = "";
                                mLeftExtra = (char)getUnicode()+"";

                                this.chosung = jongsungPrev;
                                this.jungsung = (char)primaryCode + "";
                                this.jongsung = "";
                                step = INPUT_MODE_JUNG;
                            }
                        }
                    }
                    break;
                default:
            }
        }

        private int getUnicode() {
            int cho, jung, jong;
            //초성 · ㆍ <- 둘이 달라
            if(hangul.jungsung.equals("ㆍ")){
                Log.i(TAG, "just DOT...");
                return 12685;
            }

            if (hangul.chosung.length() == 0) {
                if (hangul.jungsung.length() == 0 || hangul.jungsung.equals("ㆍ") || hangul.jungsung.equals("‥")) {
                    Log.i(TAG, "chosung X, Jungsung X, ㆍ ㆍㆍ");
                    return 0;
                }
            }

            if (hangul.chosung.equals("ㄱ")) cho = 0;
            else if (hangul.chosung.equals("ㄲ")) cho = 1;
            else if (hangul.chosung.equals("ㄴ")) cho = 2;
            else if (hangul.chosung.equals("ㄷ")) cho = 3;
            else if (hangul.chosung.equals("ㄸ")) cho = 4;
            else if (hangul.chosung.equals("ㄹ")) cho = 5;
            else if (hangul.chosung.equals("ㅁ")) cho = 6;
            else if (hangul.chosung.equals("ㅂ")) cho = 7;
            else if (hangul.chosung.equals("ㅃ")) cho = 8;
            else if (hangul.chosung.equals("ㅅ")) cho = 9;
            else if (hangul.chosung.equals("ㅆ")) cho = 10;
            else if (hangul.chosung.equals("ㅇ")) cho = 11;
            else if (hangul.chosung.equals("ㅈ")) cho = 12;
            else if (hangul.chosung.equals("ㅉ")) cho = 13;
            else if (hangul.chosung.equals("ㅊ")) cho = 14;
            else if (hangul.chosung.equals("ㅋ")) cho = 15;
            else if (hangul.chosung.equals("ㅌ")) cho = 16;
            else if (hangul.chosung.equals("ㅍ")) cho = 17;
            else /*if ( hangul.chosung.equals("ㅎ"))*/    cho = 18;

            if (hangul.jungsung.length() == 0 && hangul.jongsung.length() == 0) {
                Log.i(TAG, "Only Ja offset : " + cho + " unicode : " + 0x1100+cho);
                //쌍자음 출력해줘야지
                if(hangul.chosung.equals("ㄳ")){
                    return 12595;
                }
                else if(hangul.chosung.equals("ㄵ")){
                    return 12597;
                }
                else if(hangul.chosung.equals("ㄶ")){
                    return 12598;
                }
                else if(hangul.chosung.equals("ㄺ")){
                    return 12602;
                }
                else if(hangul.chosung.equals("ㄻ")){
                    return 12603;
                }
                else if(hangul.chosung.equals("ㄼ")){
                    return 12604;
                }
                else if(hangul.chosung.equals("ㄽ")){
                    return 12605;
                }
                else if(hangul.chosung.equals("ㄾ")){
                    return 12606;
                }
                else if(hangul.chosung.equals("ㄿ")){
                    return 12607;
                }
                else if(hangul.chosung.equals("ㅀ")){
                    return 12608;
                }
                else if(hangul.chosung.equals("ㅄ")){
                    return 12612;
                }

                return 0x1100 + cho;
            }
            if (hangul.jungsung.equals("ㆍ") || hangul.jungsung.equals("‥"))
                return 0x1100 + cho;

            // 중성
            if (hangul.jungsung.equals("ㅏ")) jung = 0;
            else if (hangul.jungsung.equals("ㅐ")) jung = 1;
            else if (hangul.jungsung.equals("ㅑ")) jung = 2;
            else if (hangul.jungsung.equals("ㅒ")) jung = 3;
            else if (hangul.jungsung.equals("ㅓ")) jung = 4;
            else if (hangul.jungsung.equals("ㅔ")) jung = 5;
            else if (hangul.jungsung.equals("ㅕ")) jung = 6;
            else if (hangul.jungsung.equals("ㅖ")) jung = 7;
            else if (hangul.jungsung.equals("ㅗ")) jung = 8;
            else if (hangul.jungsung.equals("ㅘ")) jung = 9;
            else if (hangul.jungsung.equals("ㅙ")) jung = 10;
            else if (hangul.jungsung.equals("ㅚ")) jung = 11;
            else if (hangul.jungsung.equals("ㅛ")) jung = 12;
            else if (hangul.jungsung.equals("ㅜ")) jung = 13;
            else if (hangul.jungsung.equals("ㅝ")) jung = 14;
            else if (hangul.jungsung.equals("ㅞ")) jung = 15;
            else if (hangul.jungsung.equals("ㅟ")) jung = 16;
            else if (hangul.jungsung.equals("ㅠ")) jung = 17;
            else if (hangul.jungsung.equals("ㅡ")) jung = 18;
            else if (hangul.jungsung.equals("ㅢ")) jung = 19;
            else /*if ( hangul.jungsung.equals("ㅣ"))*/    jung = 20;

            if (hangul.chosung.length() == 0 && hangul.jongsung.length() == 0) {
                Log.i(TAG, "cho empty, jong empty");
                //return 0x1161 + jung;
                return 12623 + jung;
            }

            // 종성
            if (jongsung.length() == 0) jong = 0;
            else if (jongsung.equals("ㄱ")) jong = 1;
            else if (jongsung.equals("ㄲ")) jong = 2;
            else if (jongsung.equals("ㄳ")) jong = 3;
            else if (jongsung.equals("ㄴ")) jong = 4;
            else if (jongsung.equals("ㄵ")) jong = 5;
            else if (jongsung.equals("ㄶ")) jong = 6;
            else if (jongsung.equals("ㄷ")) jong = 7;
            else if (jongsung.equals("ㄹ")) jong = 8;
            else if (jongsung.equals("ㄺ")) jong = 9;
            else if (jongsung.equals("ㄻ")) jong = 10;
            else if (jongsung.equals("ㄼ")) jong = 11;
            else if (jongsung.equals("ㄽ")) jong = 12;
            else if (jongsung.equals("ㄾ")) jong = 13;
            else if (jongsung.equals("ㄿ")) jong = 14;
            else if (jongsung.equals("ㅀ")) jong = 15;
            else if (jongsung.equals("ㅁ")) jong = 16;
            else if (jongsung.equals("ㅂ")) jong = 17;
            else if (jongsung.equals("ㅄ")) jong = 18;
            else if (jongsung.equals("ㅅ")) jong = 19;
            else if (jongsung.equals("ㅆ")) jong = 20;
            else if (jongsung.equals("ㅇ")) jong = 21;
            else if (jongsung.equals("ㅈ")) jong = 22;
            else if (jongsung.equals("ㅊ")) jong = 23;
            else if (jongsung.equals("ㅋ")) jong = 24;
            else if (jongsung.equals("ㅌ")) jong = 25;
            else if (jongsung.equals("ㅍ")) jong = 26;
            else /*if ( real_jong.equals("ㅎ"))*/    jong = 27;

            if (hangul.chosung.length() == 0 && hangul.jungsung.length() == 0)
                return 0x11a8 + jong;

            return 44032 + cho * 588 + jung * 28 + jong;
        }

        private String getHangulInputStr(int primaryCode){
            mLeftExtra = "";
            makeHangul(primaryCode);
            if(this.writingJongsungFlag){
                return "ㆍ";
            }
            return mLeftExtra+(char)getUnicode()+"";
        }

        public void deleteLastInst(){
            String lastStr = "";
            InputConnection ic = getCurrentInputConnection();

            if(!this.jongsung.equals("")) {
                this.jongsung = "";
                lastStr = (char) getUnicode() + "";
                delete(1);
                ic.commitText(lastStr, 1);
                step = INPUT_MODE_JUNG;
            }
            else if(!this.jungsung.equals("")){
                this.jungsung = "";
                lastStr = (char) getUnicode() + "";
                delete(1);
                if(!this.chosung.equals("")) {
                    ic.commitText(lastStr, 1);
                    step = INPUT_MODE_CHO;
                }
                else{
                    init();
                }
            }
            else{
                init();
                delete(1);
            }
        }
        public void delete(int count) {
            InputConnection ic = getCurrentInputConnection();
            ic.deleteSurroundingText(count, 0);
        }
        public void write(int primaryCode){
            mDelCount = 0;
            String writeStr = getHangulInputStr(primaryCode);
            Log.i(TAG, "write(" + writeStr + ") : " + this.chosung + this.jungsung + this.jongsung);
            delete(mDelCount);
            InputConnection ic = getCurrentInputConnection();
            ic.commitText(writeStr, 0);
        }
    }
    private Hangul hangul = new Hangul();

    private void getPreferenceshandedness(){
        SharedPreferences pref = getSharedPreferences("forHandedness", MODE_PRIVATE);
        handValue = pref.getInt("hand", 0);//default right
    }

    private void changeKeyboardLayout(int ikMode){
        switch(ikMode){
            case HAN_MODE:
                setKeyboardHand();
                handFlag=true;
                break;
            case ENG_MODE:
                kv.setKeyboard(keyboardEng);
                kv.setPreviewEnabled(true);
                setCurrntKeyboard(keyboardEng);
                handFlag=false;
                break;
            case NUM_MODE:
                kv.setKeyboard(keyboardNum);
                kv.setPreviewEnabled(true);
                setCurrntKeyboard(keyboardNum);
                handFlag=false;
                break;
            default:
        }
    }

    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInput(info, restarting);
        mTouchCount = mDeleteCount = 0;
        if(handFlag)
            setKeyboardHand();
        Log.e("Keyboard", "keyboard up");//키보드 업 부분
        Intent intent=new Intent();
        intent.setAction(GlobalVariable.HIDE_SERVICE);
        this.sendBroadcast(intent);
    }
    public void onFinishInputView(boolean finishingInput) {
        super.onFinishInput();
        Log.e("Keyboard", "keyboard down");//키보드 다운 부분
        Intent intent=new Intent();
        intent.setAction(GlobalVariable.DISP_SERVICE);
        this.sendBroadcast(intent);
    }
    public void setKeyboardHand()//Left인지 Rifht인지 검사
    {
        getPreferenceshandedness();
        Log.e("Keyboard","value:"+handValue);
        if(handValue==0) {
            kv.setKeyboard(keyboardRight);//right
            setCurrntKeyboard(keyboardRight);
        }
        else {
            kv.setKeyboard(keyboardLeft);//left
            setCurrntKeyboard(keyboardLeft);
        }
        //키보드 세팅시만 불러오기때문에 항상 키보드를 다시 사용할 때 계속해서 확인해줘야함
        kv.setPreviewEnabled(false);
    }
    public void setCurrntKeyboard(Keyboard currntKeyboard)
    {
        keyboard=currntKeyboard;
    }
    public void onCreate() {
        super.onCreate();
        Log.e("Keyboard", "onCreate");//키보드
        kv = (MyKeyboardView)getLayoutInflater().inflate(R.layout.customkeyboard, null);
        keyboardRight = new Keyboard(this, R.xml.hangul);
        keyboardLeft=new Keyboard(this,R.xml.hangul_left);
        keyboardEng=new Keyboard(this,R.xml.english);
        keyboardNum=new Keyboard(this,R.xml.numpad);

        setKeyboardHand();
        //kv.setKeyboard(keyboard);
        //kv.setPreviewEnabled(false);

        kv.setOnKeyboardActionListener(this);
        kv.setFocusable(true);
        kv.setFocusableInTouchMode(true);

        mService = new Intent(this, KeyboardPopupService.class);

        kv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    xPositionStart = event.getX();
                    yPositionStart = event.getY();
                    //Toast.makeText(getApplicationContext(), "X start : " + xPositionStart + " Y start : " + yPositionStart, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "X start : " + xPositionStart + " Y start : " + yPositionStart);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    xPositionEnd = event.getX();
                    yPositionEnd = event.getY();
                    //Toast.makeText(getApplicationContext(), "X END : " + xPositionEnd + " Y END : " + yPositionEnd, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "X END : " + xPositionEnd + " Y END : " + yPositionEnd);
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (currTouchKeyCode == 12641 || currTouchKeyCode == 12643 || currTouchKeyCode == 12685) {
                        double xx = event.getX();
                        double yy = event.getY();
                        for (int idx : keyboard.getNearestKeys((int) xx, (int) yy)) {
                            Keyboard.Key key = keyboard.getKeys().get(idx);
                            if (key.isInside((int) xx, (int) yy)) {
                                if (currTouchKeyCode != key.codes[0] && (key.codes[0] == 12641 || key.codes[0] == 12643 || key.codes[0] == 12685)) {
                                    prevTouchKeyCode = currTouchKeyCode;
                                    currTouchKeyCode = key.codes[0];

                                    Log.i(TAG, "prev -> curr " + prevTouchKeyCode + " -> " + currTouchKeyCode);
                                    Log.i(TAG, "key : " + key + " txt : " + key.label + " code : " + key.codes[0]);
                                    onPress(currTouchKeyCode);
                                }
                            }
                        }
                    }
                }
                return false;
            }


        });
    }
    @Override public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public View onCreateInputView(){
        super.onCreateInputView();
        return kv;
    }
    private void playClick(int keyCode){
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch (keyCode){
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD );
        }
    }

    @Override
    public void onPress(int primaryCode) {
        Log.i(TAG, "onPress start : " + primaryCode + "(" + (char) primaryCode + ")");
        prevTouchKeyCode = currTouchKeyCode = primaryCode;
        mDownKeyRotate = 0; // 키 누를 때 마다 초기화;

        switch (inputKeyboardMode) {
            case HAN_MODE:
                downKeycode = primaryCode;
                //kv.setPopupOffset(-(int) xPositionStart, -(int) yPositionStart);

                //키가 눌렸을 때, 키가 아닌 곳이 눌리면 안돼,
                if (primaryCode != 0) {
                    //모음은 누르자마자 눌려
                    if(primaryCode == 12641 || primaryCode == 12643 || primaryCode == 12685) {
                        hangul.write(primaryCode);
                        /*
                        char code = (char) primaryCode;
                        InputConnection ic = getCurrentInputConnection();
                        ic.commitText(String.valueOf(code), 1);
                        */
                    }
                    //자음 다섯개
                    else if(primaryCode == 12593 || primaryCode == 12596 || primaryCode == 12609 || primaryCode == 12613 || primaryCode == 12615) {
                        Log.i(TAG, "POPUP");
                        //popupStart(primaryCode);

                        for(Keyboard.Key k : keyboard.getKeys()){
                            if(k.codes[0] == downKeycode){
                                mDownKeyRotate = k.rotate;
                            }
                        }
                        popupArcStart(primaryCode, mDownKeyRotate);
                    }
                    else{

                    }
                }
                break;

            default:
        }
    }

    @Override
    public void onRelease(int primaryCode) {
    }
    /*
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyUp start : " + keyCode);
        return super.onKeyUp(keyCode, event);
    }
    */
    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        Log.i(TAG, "onKey start : " + primaryCode + "(" + (char) primaryCode + ")");

        popupEnd();
        InputConnection ic = getCurrentInputConnection();

        playClick(primaryCode);

//        HashMap<Integer, Runnable> hm = new HashMap<>();
//        hm.put(HAN_MODE, new Runnable() {
//            @Override
//            public void run() {
//                switch (downKeycode) {
//                    case Keyboard.KEYCODE_DELETE :
//                        //hangul.init();
//                        hangul.deleteLastInst();
//                        //ic.deleteSurroundingText(1, 0);
//                        break;
//                    case Keyboard.KEYCODE_SHIFT:
//                        caps = !caps;
//                        keyboard.setShifted(caps);
//                        kv.invalidateAllKeys();
//                        break;
//                    case Keyboard.KEYCODE_MODE_CHANGE:
//                        Log.i(TAG, "Keyboard Change han -> Eng");
//                        hangul.init();
//                        inputKeyboardMode = ENG_MODE;
//                        changeKeyboardLayout(ENG_MODE);
//                        return;
//                    //break;
//                    case 32:
//                        //space
//                        hangul.init();
//                        ic.commitText(" ", 1);
//                        break;
//                    case Keyboard.KEYCODE_DONE:
//                        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
//                        break;
//                    default:
//                        //한글 입력
//                        //모음 눌렀어 ㅡ, ㅣ, .
//                        if(downKeycode == 12641 || downKeycode == 12643 || downKeycode == 12685) {
//
//                        }
//                        //자음 눌렀겠지 자음은 올라올 때 눌려
//                        else {
//                            //primaryCode + 시작좌표 마지막좌표 판단해서 left right up down cen 판단해서 알맞는 키 입력되도록
//                            int endPrimaryCode = getPrimaryCodeByDirection((int)xPositionStart, (int)yPositionStart, (int)xPositionEnd, (int)yPositionEnd, downKeycode);
//                            Log.i(TAG, "endPC : " + endPrimaryCode);
//                            Log.i(TAG, "Ja ("+xPositionStart + ", " + yPositionStart + ") -> (" + xPositionEnd + ", " + yPositionEnd + ")");
//                            primaryCode = downKeycode;
//                            hangul.write(endPrimaryCode);
//                            /*
//                            char code = (char) primaryCode;
//                            if (Character.isLetter(code) && caps) {
//                                code = Character.toUpperCase(code);
//                            }
//                            ic.commitText(String.valueOf(code), 1);
//                            */
//                        }
//                }
//            }
//        });
//
//        hm.get(HAN_MODE).run();

        switch(inputKeyboardMode) {
            case HAN_MODE :
                switch (downKeycode) {
                    case Keyboard.KEYCODE_DELETE :
                        //hangul.init();
                        hangul.deleteLastInst();
                        //ic.deleteSurroundingText(1, 0);
                        mDeleteCount += 1;
                        break;
                    case Keyboard.KEYCODE_MODE_CHANGE:
                        Log.i(TAG, "Keyboard Change han -> Eng");
                        hangul.init();
                        inputKeyboardMode = ENG_MODE;
                        changeKeyboardLayout(ENG_MODE);
                        //return;
                        break;
                    case 32:
                        //space
                        hangul.init();
                        ic.commitText(" ", 1);
                        break;
                    case 10:
                        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                        Log.i(TAG, "HAN ENTER ");
                        break;
                    case Keyboard.KEYCODE_DONE:
                        //kv.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        Log.i(TAG, "HAN ONKEY");
                        //한글 입력
                        //모음 눌렀어 ㅡ, ㅣ, .
                        if(downKeycode == 12641 || downKeycode == 12643 || downKeycode == 12685) {

                        }
                        //자음 눌렀겠지 자음은 올라올 때 눌려
                        else {
                            //primaryCode + 시작좌표 마지막좌표 판단해서 left right up down cen 판단해서 알맞는 키 입력되도록
                            //mDownKeyRotate 이용해서 정확한 방향 구할꺼야;
                            int endPrimaryCode = getPrimaryCodeByDirection((int)xPositionStart, (int)yPositionStart, (int)xPositionEnd, (int)yPositionEnd, downKeycode);
                            Log.i(TAG, "endPC : " + endPrimaryCode);
                            Log.i(TAG, "Ja ("+xPositionStart + ", " + yPositionStart + ") -> (" + xPositionEnd + ", " + yPositionEnd + ")");
                            primaryCode = downKeycode;

                            if(endPrimaryCode > 0) {
                                hangul.write(endPrimaryCode);
                            }
                            //
                            else{
                                hangul.init();
                                //!
                                if(endPrimaryCode == -126151){
                                    ic.commitText("!", 1);
                                }
                                //핥
                                else if(endPrimaryCode == -126153){
                                    ic.commitText("♥", 1);
                                }
                                //?
                                else if(endPrimaryCode == -126154){
                                    ic.commitText("?", 1);
                                }
                                //ㅋㅋㅋ
                                else if(endPrimaryCode == -125933){
                                    ic.commitText("ㅋㅋㅋ", 1);
                                }
                            }
                            /*
                            char code = (char) primaryCode;
                            if (Character.isLetter(code) && caps) {
                                code = Character.toUpperCase(code);
                            }
                            ic.commitText(String.valueOf(code), 1);
                            */


                        }

                }
                mTouchCount += 1;
                Log.i(COUNT_TAG, "Touch Count : " + mTouchCount + ", Delete Count : " + mDeleteCount);
                break;
            case ENG_MODE :
                switch(primaryCode){
                    case Keyboard.KEYCODE_DELETE :
                        ic.deleteSurroundingText(1, 0);
                        break;
                    case Keyboard.KEYCODE_SHIFT:
                        caps = !caps;
                        keyboard.setShifted(caps);
                        kv.invalidateAllKeys();
                        break;
                    case Keyboard.KEYCODE_MODE_CHANGE:
                        Log.i(TAG, "Keyboard Change eng -> num");
                        inputKeyboardMode = HAN_MODE;
                        changeKeyboardLayout(HAN_MODE);
                        //return;
                        break;
                    case 32:
                        //space
                        ic.commitText(" ", 1);
                        break;
                    case 10:
                        Log.i(TAG, "ENG ENTER ");
                        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                        break;
                    default:
                        Log.i(TAG, "ENG ONKEY " + inputKeyboardMode);
                        char engCode = (char)primaryCode;
                        if(Character.isLetter(engCode) && caps){
                            engCode = Character.toUpperCase(engCode);
                        }
                        ic.commitText(String.valueOf(engCode), 1);
                }
                break;
            case NUM_MODE :
                switch(primaryCode){
                    case Keyboard.KEYCODE_DELETE :
                        ic.deleteSurroundingText(1, 0);
                        break;
                    case Keyboard.KEYCODE_MODE_CHANGE:
                        Log.i(TAG, "Keyboard Change num-> han");
                        inputKeyboardMode = HAN_MODE;
                        changeKeyboardLayout(HAN_MODE);
                        //return;
                        break;
                    case 32:
                        //space
                        ic.commitText(" ", 1);
                        break;
                    case Keyboard.KEYCODE_DONE:
                        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                        break;
                    default:
                        Log.i(TAG, "NUM ONKEY ");
                        char numCode = (char)primaryCode;
                        ic.commitText(String.valueOf(numCode), 1);
                }
                break;
            default:

        }
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    public void popupStart(int primaryCode){

        //Intent service = new Intent(this, KeyboardPopupService.class);

        mService.putExtra("primaryCode", primaryCode + "");
        mService.putExtra("keyboardWidth", kv.getWidth());
        mService.putExtra("keyboardHeight", kv.getHeight());

        startService(mService);
    }
    public void popupArcStart(int primaryCode, float rotate){
        //Intent service = new Intent(this, KeyboardPopupService.class);

        mService.putExtra("primaryCode", primaryCode + "");
        mService.putExtra("keyboardWidth", kv.getWidth());
        mService.putExtra("keyboardHeight", kv.getHeight());
        mService.putExtra("downKeyRotate", rotate);
        mService.putExtra("handedness", handValue);

        startService(mService);
    }

    public void popupEnd(){
        //Intent service = new Intent(this, KeyboardPopupService.class);
        stopService(mService);
    }



    //fx, fy : 시작 좌표, tx, ty : 마지막 좌표 로 방향을 리턴, threshold : 이 이상 벗어나야 방향 바뀐걸로
    private int getDerectionByPosition(int fx, int fy, int tx, int ty, int threshold){
        //제자리 = 0, 동 서 남 북 = 1, 2, 3, 4
        threshold = threshold * threshold;
        int dist = MyMath.getDist(fx, fy, tx, ty);

        double touchTan = MyMath.getTangent(fx, fy, tx, ty);

        double downKeyDegree = Math.toDegrees(mDownKeyRotate);
        //mDownKeyRotate = (float) Math.tan(downKeyDegree);
        double vx = (double)(tx-fx);
        double vy = (double)(ty-fy);
        Log.i(TAG, "Dist : " + dist + " tangent : " + touchTan + ", keytangent : " + mDownKeyRotate);
        Log.i(TAG, "(" + fx+ ", " + fy + ") -> (" + tx + ", " + ty + ") // vec(" + vx + ", " + vy + ")");
        Log.i(TAG, "TouchDeg : " + Math.toDegrees(touchTan) + ", KeyDeg : " + downKeyDegree);
        Log.i(TAG, "be Rotated Deg : " + (90-downKeyDegree));
        double rotatedDegRad = Math.toRadians(90-downKeyDegree);
        double nvx = vx*Math.cos(rotatedDegRad) - vy*Math.sin(rotatedDegRad);
        double nvy = vx*Math.sin(rotatedDegRad) + vy*Math.cos(rotatedDegRad);

        Log.i(TAG, "Normalized pos : (" + nvx + ", " + nvy + ")");

        //double tan = (touchTan + mDownKeyRotate) / (1.0 - touchTan*mDownKeyRotate);
        double tan = nvy/nvx;
        Log.i(TAG, "Normalized Tangent : " + tan);
        //일정 범위 이상 벗어나야 움직인거야
        //동서남북 1234
        if(dist > threshold){
            if(-1 < tan && tan < 1){
                Log.i(TAG, "LEFT or RIGHT");
                //서
                if(nvx < 0){
                    Log.i(TAG, "WEST");
                    return 2;
                }
                //동
                else{
                    Log.i(TAG, "EAST");
                    return 1;
                }
            }
            else{
                Log.i(TAG, "UP or DOWN");
                //북
                if(nvy < 0){
                    Log.i(TAG, "NORTH");
                    return 4;
                }
                //남
                else{
                    Log.i(TAG, "SOUTH");
                    return 3;
                }
            }
        }
        Log.i(TAG, "smaller than threshold, just center");
        return 0;
    }

    //fx, fy : 시작 좌표, tx, ty : 마지막 좌표
    private int getPrimaryCodeByDirection(int fx, int fy, int tx, int ty, int primaryCode){
        int direction = getDerectionByPosition(fx, fy, tx, ty, 30);

        //ㅁ
        if(primaryCode == 12609){
            if(direction == 0){

            }
            //ㅍ
            else if(direction == 1){
                primaryCode = 12621;
            }
            else if(direction == 2){

            }
            //ㅃ
            else if(direction == 3){
                primaryCode = 12611;
            }
            //ㅂ
            else if(direction == 4){
                primaryCode = 12610;
            }
        }
        //ㄴ
        else if(primaryCode == 12596){
            if(direction == 0){

            }
            //ㄹ
            else if(direction == 1){
                primaryCode = 12601;
            }
            //ㄷ
            else if(direction == 2){
                primaryCode = 12599;
            }
            //ㅌ
            else if(direction == 3){
                primaryCode = 12620;
            }
            //ㄸ
            else if(direction == 4){
                primaryCode = 12600;
            }
        }
        //ㅇ
        else if(primaryCode == 12615){
            if(direction == 0){

            }
            //!
            else if(direction == 1){
                primaryCode = -126151;
            }
            //ㅎ
            else if(direction == 2){
                primaryCode = 12622;
            }
            //핥
            else if(direction == 3){
                primaryCode = -126153;
            }
            //?
            else if(direction == 4){
                primaryCode = -126154;
            }
        }
        //ㅅ
        else if(primaryCode == 12613){
            if(direction == 0){

            }
            //ㅊ
            else if(direction == 1){
                primaryCode = 12618;
            }
            //ㅈ
            else if(direction == 2){
                primaryCode = 12616;
            }
            //ㅉ
            else if(direction == 3){
                primaryCode = 12617;
            }
            //ㅆ
            else if(direction == 4){
                primaryCode = 12614;
            }
        }
        //ㄱ
        else if(primaryCode == 12593){
            if(direction == 0){

            }
            else if(direction == 1){

            }
            //ㅋ
            else if(direction == 2){
                primaryCode = 12619;
            }
            //ㅋㅋㅋ
            else if(direction == 3){
                primaryCode = -125933;
            }
            //ㄲ
            else if(direction == 4){
                primaryCode = 12594;
            }
        }
        return primaryCode;
    }
}
