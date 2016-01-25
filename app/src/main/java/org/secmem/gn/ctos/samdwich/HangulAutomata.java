//
//package com.example.hellojni;
//
//import android.util.Log;
//
//import java.io.UnsupportedEncodingException;
//import java.util.Arrays;
//
///**
// * Created by 김희중 on 2016-01-11.
// */
//public class HangulAutomata {
//
//    public static int HAN_DKFODK = 12685;
//
//    public static int HAN_RLDUR = 12593; //ㄱ
//    public static int HAN_D_RLDUR = 12594; //ㄲ
//    public static int HAN_D_RLDURTLDHT = 12595; //ㄳ
//    public static int HAN_SLDMS = 12596; //ㄴ
//    public static int HAN_SLDMSWLDMT = 12597; //ㄵ
//    public static int HAN_SLDMSGLDMG = 12598; //ㄶ
//    public static int HAN_ELRME = 12599; //ㄷ
//    public static int HAN_D_ELRME = 12600; //ㄸ
//    public static int HAN_FLDMF = 12601; //ㄹ
//    public static int HAN_FLDMFRLDUR = 12602; //ㄺ
//    public static int HAN_FLDMFALDMA = 12603; //ㄻ
//    public static int HAN_FLDMFQLFMQ = 12604; //ㄼ
//    public static int HAN_FLDMFTLDHT = 12605; //ㄽ
//    public static int HAN_FLDMFXLDMX = 12606; //ㄾ
//    public static int HAN_FLDMFVLDMV = 12607; //ㄿ
//    public static int HAN_FLDMFGLDMG = 12608; //ㅀ
//    public static int HAN_ALDMA = 12609; //ㅁ
//    public static int HAN_QLDMQ = 12610; //ㅂ
//    public static int HAN_D_QLDMQ = 12611; //ㅃ
//    public static int HAN_QLDMQTLDHT = 12610; //ㅄ
//    public static int HAN_TLDHT = 12613; //ㅅ
//    public static int HAN_D_TLDHT = 12614; //ㅆ
//    public static int HAN_DLDMD = 12615; //ㅇ
//    public static int HAN_WLDMW = 12616; //ㅈ
//    public static int HAN_D_WLDMW = 12617; //ㅉ
//    public static int HAN_CLDMC = 12618; //ㅊ
//    public static int HAN_ZLDMZ = 12619; //ㅋ
//    public static int HAN_XLDMX = 12620; //ㅌ
//    public static int HAN_VLDMV = 12621; //ㅍ
//    public static int HAN_GLDMG = 12622; //ㅎ
//
//    public static int HAN_K = 12623;
//    public static int HAN_O = 12624;
//    public static int HAN_I = 12625;
//    public static int HAN_D_O = 12626;
//    public static int HAN_J = 12627;
//    public static int HAN_P = 12628;
//    public static int HAN_U = 12629;
//    public static int HAN_D_P = 12630;
//    public static int HAN_H = 12631;
//    public static int HAN_HK = 12632;
//    public static int HAN_HO = 12633;
//    public static int HAN_HL = 12634;
//    public static int HAN_Y = 12635;
//    public static int HAN_N = 12636;
//    public static int HAN_NJ = 12637;
//    public static int HAN_N_D_P = 12638;
//    public static int HAN_NL = 12639;
//    public static int HAN_B = 12640;
//    public static int HAN_M = 12641;
//    public static int HAN_ML = 12642;
//    public static int HAN_L = 12643;
//
//
//    //<Key android:codes="12685" android:keyLabel="ㆍ"/>
//    // ㄱ  ㄲ  ㄴ  ㄷ  ㄸ  ㄹ  ㅁ  ㅂ  ㅃ  ㅅ  ㅆ  ㅇ  ㅈ  ㅉ  ㅊ  ㅋ  ㅌ  ㅍ  ㅎ
//    private static final int[] PREF_CHO = {
//            12593, 12594, 12596, 12599, 12600, 12601, 12609, 12610,
//            12611, 12613, 12614, 12615, 12616, 12617, 12618, 12619,
//            12620, 12621, 12622};
//    // ㅏ  ㅐ  ㅑ  ㅒ  ㅓ  ㅔ  ㅕ  ㅖ  ㅗ  ㅘ  ㅙ  ㅚ  ㅛ  ㅜ  ㅝ  ㅞ  ㅟ  ㅠ  ㅡ  ㅢ  ㅣ
//    private static final int[] PREF_JUNG = {
//            12623, 12624, 12625, 12626, 12627, 12628, 12629, 12630,
//            12631, 12632, 12633, 12634, 12635, 12636, 12637, 12638,
//            12639, 12640, 12641, 12642, 12643};
//    // ㄱ  ㄲ  ㄳ  ㄴ  ㄵ  ㄶ  ㄷ  ㄹ  ㄺ  ㄻ  ㄼ  ㄽ  ㄾ  ㄿ  ㅀ  ㅁ  ㅂ  ㅄ  ㅅ  ㅆ  ㅇ  ㅈ  ㅊ  ㅋ  ㅌ  ㅍ  ㅎ
//    private static final int[] PREF_JONG = {
//            12593, 12594, 12595, 12596, 12597, 12598, 12599, 12601,
//            12602, 12603, 12604, 12605, 12606, 12607, 12608, 12609,
//            12610, 12612, 12613, 12614, 12615, 12616, 12618, 12619,
//            12620, 12621, 12622};
//
//    private static final String mChoseong = new String(PREF_CHO, 0, PREF_CHO.length);
//    private static final String mJungseong = new String(PREF_JUNG, 0, PREF_JUNG.length);
//
//    public static final int HANGUL_NONE = -1;
//    public static final int HANGUL_JA = 0;
//    public static final int HANGUL_MO = 1;
//
//    public static final int HANGUL_CHO1 = 0;
//    public static final int HANGUL_CHO2 = 1;
//    public static final int HANGUL_JUNG1 = 2;
//    public static final int HANGUL_JUNG2 = 3;
//    public static final int HANGUL_JONG1 = 4;
//    public static final int HANGUL_JONG2 = 5;
//    public static final int HANGUL_FINISH1 = 6;
//    public static final int HANGUL_FINISH2 = 7;
//    public static final int HANGUL_FINISH3 = 8;
//    public static final int HANGUL_FINISH4 = 9;
//
//    private int mCurrentState = HANGUL_NONE;
//    private int mHangulCharBuffer[] = new int[3];
//
//    private int mWorkingChar;
//
//    private class Hangul
//    {
//        public String chosung = "";
//        public String jungsung = "";
//        public String jongsung = "";
//        public String jongsung2 = "";
//        public int step = 0;
//        public boolean flag_writing = false;
//        public boolean flag_dotused = false;
//        public boolean flag_doubled = false;
//        public boolean flag_addcursor = false;
//        private boolean flag_space = false;
//        public void init()
//        {
//            this.chosung = "";
//            this.jungsung = "";
//            this.jongsung = "";
//            this.jongsung2 = "";
//            this.step = 0;
//            this.flag_writing = false;
//            this.flag_dotused = false;
//            this.flag_doubled = false;
//            this.flag_addcursor = false;
//            this.flag_space = false;
//        }
//    }
//    private Hangul hangul = new Hangul();
//
//    private int getUnicode(String real_jong)
//    {
//        int cho, jung, jong;
//        //초성
//        if(hangul.chosung.length() == 0)
//        {
//            if(hangul.jungsung.length() == 0 || hangul.jungsung.equals("·") || hangul.jungsung.equals("‥"))
//                return 0;
//        }
//
//        if ( hangul.chosung.equals("ㄱ"))	cho = 0;
//        else if ( hangul.chosung.equals("ㄲ"))	cho = 1;
//        else if ( hangul.chosung.equals("ㄴ"))	cho = 2;
//        else if ( hangul.chosung.equals("ㄷ"))	cho = 3;
//        else if ( hangul.chosung.equals("ㄸ"))	cho = 4;
//        else if ( hangul.chosung.equals("ㄹ"))	cho = 5;
//        else if ( hangul.chosung.equals("ㅁ"))	cho = 6;
//        else if ( hangul.chosung.equals("ㅂ"))	cho = 7;
//        else if ( hangul.chosung.equals("ㅃ"))	cho = 8;
//        else if ( hangul.chosung.equals("ㅅ"))	cho = 9;
//        else if ( hangul.chosung.equals("ㅆ"))	cho = 10;
//        else if ( hangul.chosung.equals("ㅇ"))	cho = 11;
//        else if ( hangul.chosung.equals("ㅈ"))	cho = 12;
//        else if ( hangul.chosung.equals("ㅉ"))	cho = 13;
//        else if ( hangul.chosung.equals("ㅊ"))	cho = 14;
//        else if ( hangul.chosung.equals("ㅋ"))	cho = 15;
//        else if ( hangul.chosung.equals("ㅌ"))	cho = 16;
//        else if ( hangul.chosung.equals("ㅍ"))	cho = 17;
//        else /*if ( hangul.chosung.equals("ㅎ"))*/	cho = 18;
//
//        if (hangul.jungsung.length() == 0 && hangul.jongsung.length() == 0)
//            return 0x1100 + cho;
//        if (hangul.jungsung.equals("·") || hangul.jungsung.equals("‥"))
//            return 0x1100 + cho;
//
//        // 중성
//        if ( hangul.jungsung.equals("ㅏ"))		jung = 0;
//        else if ( hangul.jungsung.equals("ㅐ"))	jung = 1;
//        else if ( hangul.jungsung.equals("ㅑ"))	jung = 2;
//        else if ( hangul.jungsung.equals("ㅒ"))	jung = 3;
//        else if ( hangul.jungsung.equals("ㅓ"))	jung = 4;
//        else if ( hangul.jungsung.equals("ㅔ"))	jung = 5;
//        else if ( hangul.jungsung.equals("ㅕ"))	jung = 6;
//        else if ( hangul.jungsung.equals("ㅖ"))	jung = 7;
//        else if ( hangul.jungsung.equals("ㅗ"))	jung = 8;
//        else if ( hangul.jungsung.equals("ㅘ"))	jung = 9;
//        else if ( hangul.jungsung.equals("ㅙ"))	jung = 10;
//        else if ( hangul.jungsung.equals("ㅚ"))	jung = 11;
//        else if ( hangul.jungsung.equals("ㅛ"))	jung = 12;
//        else if ( hangul.jungsung.equals("ㅜ"))	jung = 13;
//        else if ( hangul.jungsung.equals("ㅝ"))	jung = 14;
//        else if ( hangul.jungsung.equals("ㅞ"))	jung = 15;
//        else if ( hangul.jungsung.equals("ㅟ"))	jung = 16;
//        else if ( hangul.jungsung.equals("ㅠ"))	jung = 17;
//        else if ( hangul.jungsung.equals("ㅡ"))	jung = 18;
//        else if ( hangul.jungsung.equals("ㅢ"))	jung = 19;
//        else /*if ( hangul.jungsung.equals("ㅣ"))*/	jung = 20;
//
//        if ( hangul.chosung.length() == 0 && hangul.jongsung.length() == 0)
//            return 0x1161 + jung;
//
//        // 종성
//        if ( real_jong.length() == 0)		jong = 0;
//        else if ( real_jong.equals("ㄱ"))	jong = 1;
//        else if ( real_jong.equals("ㄲ"))	jong = 2;
//        else if ( real_jong.equals("ㄳ"))	jong = 3;
//        else if ( real_jong.equals("ㄴ"))	jong = 4;
//        else if ( real_jong.equals("ㄵ"))	jong = 5;
//        else if ( real_jong.equals("ㄶ"))	jong = 6;
//        else if ( real_jong.equals("ㄷ"))	jong = 7;
//        else if ( real_jong.equals("ㄹ"))	jong = 8;
//        else if ( real_jong.equals("ㄺ"))	jong = 9;
//        else if ( real_jong.equals("ㄻ"))	jong = 10;
//        else if ( real_jong.equals("ㄼ"))	jong = 11;
//        else if ( real_jong.equals("ㄽ"))	jong = 12;
//        else if ( real_jong.equals("ㄾ"))	jong = 13;
//        else if ( real_jong.equals("ㄿ"))	jong = 14;
//        else if ( real_jong.equals("ㅀ"))	jong = 15;
//        else if ( real_jong.equals("ㅁ"))	jong = 16;
//        else if ( real_jong.equals("ㅂ"))	jong = 17;
//        else if ( real_jong.equals("ㅄ"))	jong = 18;
//        else if ( real_jong.equals("ㅅ"))	jong = 19;
//        else if ( real_jong.equals("ㅆ"))	jong = 20;
//        else if ( real_jong.equals("ㅇ"))	jong = 21;
//        else if ( real_jong.equals("ㅈ"))	jong = 22;
//        else if ( real_jong.equals("ㅊ"))	jong = 23;
//        else if ( real_jong.equals("ㅋ"))	jong = 24;
//        else if ( real_jong.equals("ㅌ"))	jong = 25;
//        else if ( real_jong.equals("ㅍ"))	jong = 26;
//        else /*if ( real_jong.equals("ㅎ"))*/	jong = 27;
//
//        if ( hangul.chosung.length() == 0 && hangul.jungsung.length() == 0)
//            return 0x11a8 + jong;
//
//        return 44032 + cho*588 + jung*28 + jong;
//    }
//
//    public HangulAutomata()
//    {
//        reset();
//    }
//
//
//    private void delete()
//    {
//        int position = et.getSelectionStart();
//        if(position == 0)
//            return;
//
//        String origin = "";
//        String str = "";
//
//        origin = et.getText().toString();
//        str += origin.substring(0, position-1);
//        str += origin.substring(position, origin.length());
//        et.setText(str);
//        et.setSelection(position - 1);
//    }
//
//    private void hangulMake(int input)
//    {
//        String beforedata = "";
//        String nowdata = "";
//        String overdata = "";
//        if(input == 10) //띄어쓰기
//        {
//            if(hangul.flag_writing)
//                hangul.init();
//            else
//                hangul.flag_space = true;
//        }
//        else if(input == 11) //지우기
//        {
//            if(hangul.step == 0)
//            {
//                if(hangul.chosung.length() == 0)
//                {
//                    delete();
//                    hangul.flag_writing = false;
//                }
//                else
//                    hangul.chosung = "";
//            }
//            else if(hangul.step == 1)
//            {
//                if(hangul.jungsung.equals("·") || hangul.jungsung.equals("‥"))
//                {
//                    delete();
//                    if(hangul.chosung.length() == 0)
//                        hangul.flag_writing = false;
//                }
//                hangul.jungsung = "";
//                hangul.step = 0;
//            }
//            else if(hangul.step == 2)
//            {
//                hangul.jongsung = "";
//                hangul.step = 1;
//            }
//            else if(hangul.step == 3)
//            {
//                hangul.jongsung2 = "";
//                hangul.step = 2;
//            }
//        }
//        else if(input == 1 || input == 2 || input == 3) //모음
//        {
//            //받침에서 떼어오는거 추가해야함
//            boolean batchim = false;
//            if(hangul.step == 2)
//            {
//                delete();
//                String s = hangul.jongsung;
//                hangul.jongsung = "";
//                hangul.flag_writing = false;
//                write(now_mode);
//                hangul.init();
//                hangul.chosung = s;
//                hangul.step = 0;
//                batchim = true;
//            }
//            else if(hangul.step == 3)
//            {
//                String s = hangul.jongsung2;
//                if(hangul.flag_doubled)
//                    delete();
//                else
//                {
//                    delete();
//                    hangul.jongsung2 = "";
//                    hangul.flag_writing = false;
//                    write(now_mode);
//                }
//                hangul.init();
//                hangul.chosung = s;
//                hangul.step = 0;
//                batchim = true;
//            }
//            beforedata = hangul.jungsung;
//            hangul.step = 1;
//            if(input == 1) // ㅣ ㅓ ㅕ ㅐ ㅔ ㅖㅒ ㅚ ㅟ ㅙ ㅝ ㅞ ㅢ
//            {
//                if(beforedata.length() == 0)		nowdata = "ㅣ";
//                else if(beforedata.equals("·"))
//                {
//                    nowdata = "ㅓ";
//                    hangul.flag_dotused = true;
//                }
//                else if(beforedata.equals("‥"))
//                {
//                    nowdata = "ㅕ";
//                    hangul.flag_dotused = true;
//                }
//                else if(beforedata.equals("ㅏ"))	nowdata = "ㅐ";
//                else if(beforedata.equals("ㅑ"))	nowdata = "ㅒ";
//                else if(beforedata.equals("ㅓ"))	nowdata = "ㅔ";
//                else if(beforedata.equals("ㅕ"))	nowdata = "ㅖ";
//                else if(beforedata.equals("ㅗ"))	nowdata = "ㅚ";
//                else if(beforedata.equals("ㅜ"))	nowdata = "ㅟ";
//                else if(beforedata.equals("ㅠ"))	nowdata = "ㅝ";
//                else if(beforedata.equals("ㅘ"))	nowdata = "ㅙ";
//                else if(beforedata.equals("ㅝ"))	nowdata = "ㅞ";
//                else if(beforedata.equals("ㅡ"))	nowdata = "ㅢ";
//                else
//                {
//                    hangul.init();
//                    hangul.step = 1;
//                    nowdata = "ㅣ";
//                }
//            }
//            else if(input == 2) // ·,‥,ㅏ,ㅑ,ㅜ,ㅠ,ㅘ
//            {
//                if(beforedata.length() == 0)
//                {
//                    nowdata = "·";
//                    if(batchim)
//                        hangul.flag_addcursor = true;
//                }
//                else if(beforedata.equals("·"))
//                {
//                    nowdata = "‥";
//                    hangul.flag_dotused = true;
//                }
//                else if(beforedata.equals("‥"))
//                {
//                    nowdata = "·";
//                    hangul.flag_dotused = true;
//                }
//                else if(beforedata.equals("ㅣ"))	nowdata = "ㅏ";
//                else if(beforedata.equals("ㅏ"))	nowdata = "ㅑ";
//                else if(beforedata.equals("ㅡ"))	nowdata = "ㅜ";
//                else if(beforedata.equals("ㅜ"))	nowdata = "ㅠ";
//                else if(beforedata.equals("ㅚ"))	nowdata = "ㅘ";
//                else
//                {
//                    hangul.init();
//                    hangul.step = 1;
//                    nowdata = "·";
//                }
//            }
//            else if(input == 3) // ㅡ, ㅗ, ㅛ
//            {
//                if(beforedata.length() == 0)		nowdata = "ㅡ";
//                else if(beforedata.equals("·"))
//                {
//                    nowdata = "ㅗ";
//                    hangul.flag_dotused = true;
//                }
//                else if(beforedata.equals("‥"))
//                {
//                    nowdata = "ㅛ";
//                    hangul.flag_dotused = true;
//                }
//                else
//                {
//                    hangul.init();
//                    hangul.step = 1;
//                    nowdata = "ㅡ";
//                }
//            }
//            hangul.jungsung = nowdata;
//        }
//        else //자음
//        {
//            if(hangul.step == 1)
//            {
//                if(hangul.jungsung.equals("·") || hangul.jungsung.equals("‥"))
//                    hangul.init();
//                else
//                    hangul.step = 2;
//            }
//            if(hangul.step == 0)		beforedata = hangul.chosung;
//            else if(hangul.step == 2)	beforedata = hangul.jongsung;
//            else if(hangul.step == 3)	beforedata = hangul.jongsung2;
//
//            if(input == 4) // ㄱ, ㅋ, ㄲ, ㄺ
//            {
//                if(beforedata.length() == 0)
//                {
//                    if(hangul.step == 2)
//                    {
//                        if(hangul.chosung.length() == 0)
//                            overdata = "ㄱ";
//                        else
//                            nowdata = "ㄱ";
//                    }
//                    else
//                        nowdata = "ㄱ";
//                }
//                else if(beforedata.equals("ㄱ"))
//                    nowdata = "ㅋ";
//                else if(beforedata.equals("ㅋ"))
//                    nowdata = "ㄲ";
//                else if(beforedata.equals("ㄲ"))
//                    nowdata = "ㄱ";
//                else if(beforedata.equals("ㄹ") && hangul.step == 2)
//                {
//                    hangul.step = 3;
//                    nowdata = "ㄱ";
//                }
//                else
//                    overdata = "ㄱ";
//            }
//            else if(input == 5) // ㄴ ㄹ
//            {
//                if (beforedata.length() == 0)
//                {
//                    if(hangul.step == 2)
//                    {
//                        if(hangul.chosung.length() == 0)
//                            overdata = "ㄴ";
//                        else
//                            nowdata = "ㄴ";
//                    }
//                    else
//                        nowdata = "ㄴ";
//                }
//                else if (beforedata.equals("ㄴ"))
//                    nowdata = "ㄹ";
//                else if (beforedata.equals("ㄹ"))
//                    nowdata = "ㄴ";
//                else
//                    overdata = "ㄴ";
//            }
//            else if(input == 6) // ㄷ, ㅌ, ㄸ, ㄾ
//            {
//                if (beforedata.length() == 0)
//                {
//                    if(hangul.step == 2)
//                    {
//                        if(hangul.chosung.length() == 0)
//                            overdata = "ㄷ";
//                        else
//                            nowdata = "ㄷ";
//                    }
//                    else
//                        nowdata = "ㄷ";
//                }
//                else if (beforedata.equals("ㄷ"))
//                    nowdata = "ㅌ";
//                else if (beforedata.equals("ㅌ"))
//                    nowdata = "ㄸ";
//                else if (beforedata.equals("ㄸ"))
//                    nowdata = "ㄷ";
//                else if(beforedata.equals("ㄹ") && hangul.step == 2)
//                {
//                    hangul.step = 3;
//                    nowdata = "ㄷ";
//                }
//                else
//                    overdata = "ㄷ";
//            }
//            else if(input == 7) // ㅂ, ㅍ, ㅃ, ㄼ, ㄿ
//            {
//                if (beforedata.length() == 0)
//                {
//                    if(hangul.step == 2)
//                    {
//                        if(hangul.chosung.length() == 0)
//                            overdata = "ㅂ";
//                        else
//                            nowdata = "ㅂ";
//                    }
//                    else
//                        nowdata = "ㅂ";
//                }
//                else if (beforedata.equals("ㅂ"))
//                    nowdata = "ㅍ";
//                else if (beforedata.equals("ㅍ"))
//                    nowdata = "ㅃ";
//                else if (beforedata.equals("ㅃ"))
//                    nowdata = "ㅂ";
//                else if(beforedata.equals("ㄹ") && hangul.step == 2)
//                {
//                    hangul.step = 3;
//                    nowdata = "ㅂ";
//                }
//                else
//                    overdata = "ㅂ";
//            }
//            else if(input == 8) // ㅅ, ㅎ, ㅆ, ㄳ, ㄶ, ㄽ, ㅀ, ㅄ
//            {
//                if (beforedata.length() == 0)
//                {
//                    if(hangul.step == 2)
//                    {
//                        if(hangul.chosung.length() == 0)
//                            overdata = "ㅅ";
//                        else
//                            nowdata = "ㅅ";
//                    }
//                    else
//                        nowdata = "ㅅ";
//                }
//                else if (beforedata.equals("ㅅ"))
//                    nowdata = "ㅎ";
//                else if (beforedata.equals("ㅎ"))
//                    nowdata = "ㅆ";
//                else if (beforedata.equals("ㅆ"))
//                    nowdata = "ㅅ";
//                else if(beforedata.equals("ㄱ") && hangul.step == 2)
//                {
//                    hangul.step = 3;
//                    nowdata = "ㅅ";
//                }
//                else if(beforedata.equals("ㄴ") && hangul.step == 2)
//                {
//                    hangul.step = 3;
//                    nowdata = "ㅅ";
//                }
//                else if(beforedata.equals("ㄹ") && hangul.step == 2)
//                {
//                    hangul.step = 3;
//                    nowdata = "ㅅ";
//                }
//                else if(beforedata.equals("ㅂ") && hangul.step == 2)
//                {
//                    hangul.step = 3;
//                    nowdata = "ㅅ";
//                }
//                else
//                    overdata = "ㅅ";
//            }
//            else if(input == 9) // ㅈ, ㅊ, ㅉ, ㄵ
//            {
//                if (beforedata.length() == 0)
//                {
//                    if(hangul.step == 2)
//                    {
//                        if(hangul.chosung.length() == 0)
//                            overdata = "ㅈ";
//                        else
//                            nowdata = "ㅈ";
//                    }
//                    else
//                        nowdata = "ㅈ";
//                }
//                else if (beforedata.equals("ㅈ"))
//                    nowdata = "ㅊ";
//                else if (beforedata.equals("ㅊ"))
//                    nowdata = "ㅉ";
//                else if (beforedata.equals("ㅉ"))
//                    nowdata = "ㅈ";
//                else if(beforedata.equals("ㄴ") && hangul.step == 2)
//                {
//                    hangul.step = 3;
//                    nowdata = "ㅈ";
//                }
//                else
//                    overdata = "ㅈ";
//            }
//            else if(input == 0) // ㅇ, ㅁ, ㄻ
//            {
//                if (beforedata.length() == 0)
//                {
//                    if(hangul.step == 2)
//                    {
//                        if(hangul.chosung.length() == 0)
//                            overdata = "ㅇ";
//                        else
//                            nowdata = "ㅇ";
//                    }
//                    else
//                        nowdata = "ㅇ";
//                }
//                else if (beforedata.equals("ㅇ"))
//                    nowdata = "ㅁ";
//                else if (beforedata.equals("ㅁ"))
//                    nowdata = "ㅇ";
//                else if(beforedata.equals("ㄹ") && hangul.step == 2)
//                {
//                    hangul.step = 3;
//                    nowdata = "ㅇ";
//                }
//                else
//                    overdata = "ㅇ";
//            }
//
//            if(nowdata.length() > 0)
//            {
//                if(hangul.step == 0)
//                    hangul.chosung = nowdata;
//                else if(hangul.step == 2)
//                    hangul.jongsung = nowdata;
//                else //if(hangul.step == 3)
//                    hangul.jongsung2 = nowdata;
//            }
//            if(overdata.length() > 0)
//            {
//                hangul.flag_writing = false;
//                hangul.init();
//                hangul.chosung = overdata;
//            }
//        }
//    }
//
//    public void reset()
//    {
//        //Log.v("SoftKeyboard", "reset");
//        mCurrentState = HANGUL_NONE;
//        Arrays.fill(mHangulCharBuffer, -1);
//        mWorkingChar = -1;
//    }
//
//    private int getChoseongIndex(int primaryCode)
//    {
//        for(int i = 0; i < PREF_CHO.length; i++)
//        {
//            if(primaryCode == PREF_CHO[i])
//                return i;
//        }
//
//        return -1;
//    }
//
//    private int getJungseongIndex(int primaryCode)
//    {
//        for(int i = 0; i < PREF_JUNG.length; i++)
//        {
//            if(primaryCode == PREF_JUNG[i])
//                return i;
//        }
//
//        return -1;
//    }
//
//    private int getJongseongIndex(int primaryCode)
//    {
//        for(int i = 0; i < PREF_JONG.length; i++)
//        {
//            if(primaryCode == PREF_JONG[i])
//                return i;
//        }
//
//        return -1;
//    }
//
//    private static boolean isJungseongPair(int v)
//    {
//        switch(v)
//        {
//            case 12632:
//            case 12633:
//            case 12634:
//            case 12637:
//            case 12638:
//            case 12639:
//            case 12642:
//                return true;
//            default:
//                return false;
//        }
//    }
//
//    private static int[] resolveJungseongPair(int v)
//    {
//        int r[] = new int[2];
//        switch(v)
//        {
//            case 12632:
//                r[0] = 12631;
//                r[1] = 12623;
//                break;
//            case 12633:
//                r[0] = 12631;
//                r[1] = 12624;
//                break;
//            case 12634:
//                r[0] = 12631;
//                r[1] = 12643;
//                break;
//            case 12637:
//                r[0] = 12636;
//                r[1] = 12627;
//                break;
//            case 12638:
//                r[0] = 12636;
//                r[1] = 12628;
//                break;
//            case 12639:
//                r[0] = 12636;
//                r[1] = 12643;
//                break;
//            case 12642:
//                r[0] = 12641;
//                r[1] = 12643;
//                break;
//            default:
//                r[0] = -1;
//                r[1] = -1;
//                break;
//        }
//
//        return r;
//    }
//
//    private int getJungseongPair(int v1, int v2)
//    {
//        switch(v1)
//        {
//            case 12631:
//                switch(v2)
//                {
//                    case 12623:
//                        return 12632;
//                    case 12624:
//                        return 12633;
//                    case 12643:
//                        return 12634;
//                    default:
//                        break;
//                }
//                break;
//            case 12636:
//                switch(v2)
//                {
//                    case 12627:
//                        return 12637;
//                    case 12628:
//                        return 12638;
//                    case 12643:
//                        return 12639;
//                    default:
//                        break;
//                }
//                break;
//            case 12641:
//                switch(v2)
//                {
//                    case 12643:
//                        return 12642;
//                    default:
//                        break;
//                }
//                break;
//            default:
//                break;
//        }
//        return -1;
//    }
//
//    private static boolean isJongseongPair(int v)
//    {
//        switch(v)
//        {
//            case 12595:
//            case 12597:
//            case 12598:
//            case 12602:
//            case 12603:
//            case 12604:
//            case 12605:
//            case 12606:
//            case 12607:
//            case 12608:
//            case 12612:
//                return true;
//            default:
//                return false;
//        }
//    }
//
//    private static int[] resolveJongseongPair(int v)
//    {
//        int r[] = new int[2];
//        switch(v)
//        {
//            case 12595:
//                r[0] = 12593;
//                r[1] = 12613;
//                break;
//            case 12597:
//                r[0] = 12596;
//                r[1] = 12616;
//                break;
//            case 12598:
//                r[0] = 12596;
//                r[1] = 12622;
//                break;
//            case 12602:
//                r[0] = 12601;
//                r[1] = 12593;
//                break;
//            case 12603:
//                r[0] = 12601;
//                r[1] = 12609;
//                break;
//            case 12604:
//                r[0] = 12601;
//                r[1] = 12610;
//                break;
//            case 12605:
//                r[0] = 12601;
//                r[1] = 12613;
//                break;
//            case 12606:
//                r[0] = 12601;
//                r[1] = 12620;
//                break;
//            case 12607:
//                r[0] = 12601;
//                r[1] = 12621;
//                break;
//            case 12608:
//                r[0] = 12601;
//                r[1] = 12622;
//                break;
//            case 12612:
//                r[0] = 12610;
//                r[1] = 12613;
//                break;
//            default:
//                r[0] = -1;
//                r[1] = -1;
//                break;
//        }
//
//        return r;
//    }
//
//    private static int getJongseongPair(int v1, int v2)
//    {
//        switch(v1)
//        {
//            case 12593:
//                switch(v2)
//                {
//                    case 12613:
//                        return 12595;
//                    default:
//                        break;
//                }
//                break;
//            case 12596:
//                switch(v2)
//                {
//                    case 12616:
//                        return 12597;
//                    case 12622:
//                        return 12598;
//                    default:
//                        break;
//                }
//                break;
//            case 12601:
//                switch(v2)
//                {
//                    case 12593:
//                        return 12602;
//                    case 12609:
//                        return 12603;
//                    case 12610:
//                        return 12604;
//                    case 12613:
//                        return 12605;
//                    case 12620:
//                        return 12606;
//                    case 12621:
//                        return 12607;
//                    case 12622:
//                        return 12608;
//                    default:
//                        break;
//                }
//                break;
//            case 12610:
//                switch(v2)
//                {
//                    case 12613:
//                        return 12612;
//                    default:
//                        break;
//                }
//                break;
//            default:
//                break;
//        }
//
//        return -1;
//    }
//
//    public static String encode(String text)
//    {
//        String out = new String();
//
//        if(null != text && text.length() > 0)
//        {
//            HangulAutomata ha = new HangulAutomata();
//
//            int[] ret;
//            for(int i =0; i < text.length(); i++)
//            {
//                ret = ha.appendCharacter((int)text.charAt(i));
//                if(-1 != ret[0])
//                    out += (char)ret[0];
//                if(-1 != ret[1])
//                    out += (char)ret[1];
//
//            }
//
//            int c = ha.getBuffer();
//            if(-1 != c)
//                out += (char)c;
//        }
//        return out;
//    }
//
//    public static int countCharacter(char c)
//    {
//        if(c >= 0X3131 && c <= 0X3163)
//        {
//            if(isJongseongPair((int)c))
//                return 2;
//            else if(isJungseongPair((int)c))
//                return 2;
//            else
//                return 1;
//        }
//        else if(c >= 0XAC00 && c <= 0XD7A3)
//        {
//            int n;
//            int value = c - 0xAC00;
//            int divJong  =  value / 28;
//            int /*cho = -1,*/ jung = -1, jong = -1;
//            n = value % 28;
//            if(n > 0)
//                jong = PREF_JONG[n - 1];
//
//            //int divJung = divJong / 21;
//            n = divJong % 21;
//            jung = PREF_JUNG[n];
//
//            //n = divJung % 19;
//            //cho = PREF_CHO[n];
//
//            n = 0;
//            if(-1 != jong)
//                n += isJongseongPair(jong) ? 2 : 1;
//            if(-1 != jung)
//                n += isJungseongPair(jung) ? 2 : 1;
//            n++;
//
//            return n;
//        }
//        else
//            return 1;
//    }
//
//    public static String decode(String text)
//    {
//        String out = new String();
//        int[] buffer = new int[5];
//        char c;
//
//        for(int i = 0; i < text.length(); i++)
//        {
//            c = text.charAt(i);
//            if(c >= 0X3131 && c <= 0X3163)
//            {
//                if(isJongseongPair((int)c))
//                {
//                    int[] jong = resolveJongseongPair(c);
//                    out += (char)jong[0];
//                    out += (char)jong[1];
//                }
//                else if(isJungseongPair((int)c))
//                {
//                    int[] jung = resolveJungseongPair(c);
//                    out += (char)jung[0];
//                    out += (char)jung[1];
//                }
//                else
//                    out +=c;
//            }
//            else if(c >= 0XAC00 && c <= 0XD7A3)
//            {
//                Arrays.fill(buffer, -1);
//                int n;
//                int value = c - 0xAC00;
//                int divJong  =  value / 28;
//                n = value % 28;
//                if(n > 0)
//                    buffer[3] = PREF_JONG[n - 1];
//
//                int divJung = divJong / 21;
//                n = divJong % 21;
//                buffer[1] = PREF_JUNG[n];
//
//                n = divJung % 19;
//                buffer[0] = PREF_CHO[n];
//
//                if(-1 != buffer[3])
//                {
//                    if(isJongseongPair(buffer[3]))
//                    {
//                        int[] jong = resolveJongseongPair(buffer[3]);
//                        buffer[3] = jong[0];
//                        buffer[4] = jong[1];
//                    }
//                }
//                if(-1 != buffer[1])
//                {
//                    if(isJungseongPair(buffer[1]))
//                    {
//                        int[] jung = resolveJungseongPair(buffer[1]);
//                        buffer[1] = jung[0];
//                        buffer[2] = jung[1];
//                    }
//                }
//                for(int j = 0; j < buffer.length; j++)
//                {
//                    if(-1 != buffer[j])
//                        out += (char)buffer[j];
//                }
//            }
//            else
//                out +=c;
//        }
//
//        //Log.v("SoftKeyboard", "decode: " + "[" + encodingString(out) + "]");
//        return out;
//    }
//
//}
