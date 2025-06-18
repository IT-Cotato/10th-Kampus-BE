package com.cotato.kampus.domain.cert.enums;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UnivMail {

	KAYA("Kaya University", "kaya"), GACHON("Gachon University","gachon"), GACHON_MEDICAL("Gachon University of Medical Science", "gachon"),
	CATHOLIC_KWANDONG("Catholic Kwandong University", "cku"), CATHOLIC("Catholic University of Korea", "catholic"), CATHOLIC_SANGJI("Catholic Sangji University", "mtu"),
	KANGNAM("Kangnam University", "Kangnam"), KANGDONG("Kangdong University", "Kangdong"),
	GANGEOUNG_YOUNGDONG("Gangneung-Yeongdong University", "gyc"), GANGEOUNG_WONJU("Gangneung-Wonju National University", "gwnu"),
	KANGWON("Kangwon National University","kangwon"), GANGWON("Gangwon Provincial University", "gw"),
	KOJE("Geoje University", "koje"), KONKUK("Konkuk University","konkuk"), KKU("Konkuk University (GLOCAL Campus)","kku"),
	KONYANG("Konyang University", "konyang"), GTEC("Gyeonggi University of Science and Technology","gtec"), KYONGGI("Kyonggi University","kyonggi"),
	KYUNGNAM("Kyungnam University", "kyungnam"), GC("Gyeongnam Provincial Geochang College", "gc"), NAMHAE("Gyeongnam Provincial Namhae College", "namhae"),
	KIT("Kyungnam College of Information & Technology", "kit"), KYUNGMIN("Kyungmin University", "kyungmin"),
	KBU("Kyungbok University", "kbu"), KBSU("Kyungbuk Science College", "kbsu"),  KNU("Kyungpook National University","knu"),
	GPC("Kyungbuk Provincial College", "gpc"), YFLC("Youngnam Foreign Language College", "yflc"),
	KBC("Kyungbuk College of Science & Technology", "kbc"), DHU("Daegu Haany University", "dhu"),
	GNU("Gyeongsang National University", "gnu"), KS("Kyungsung University", "ks"),
	IKW("Kyungwoon University", "ikw"), GINUE("Gyeongin National University of Education", "ginue"),
	KIWU("Kyungin Women's University", "kiwu"), KIU("Kyungil University", "kiu"), GJU("Kyongju University", "gu"),
	KHU("Kyung Hee University","khu"), KHCU("Kyung Hee Cyber University", "khcu"), KMU("Keimyung University", "kmu"),
	KMCU("Keimyung College University", "kmcu"), KAYWON("Kaywon University of Art & Design", "kaywon"),
	NAJU("Naju Collage", "naju"), KOREA("Korea University", "korea"), KOREA_SEJONG("Korea University (Sejong Campus)", "korea"),
	CUK("Korea Cyber University", "cuk"), GJUE("Kongju National University of Education", "kongju"), KONGJU("Kongju National University", "kongju"),
	KWANGSIN("Kwangshin University", "kwangshin"), GY("Gwangyang Health University College", "gy"),
	KW("Kwangwoon University","kw"), GJC("Gwangju Catholic University", "gjc"), GNUE("Gwangju National University of Education", "gnue"),
	GWANGJU("Gwangju University", "gwangju"), GHU("Gwangju Health University", "ghu"), GUMI("Gumi University", "gumi"),
	GUFOT("The Salvation Army College", "gufot"), KOOKMIN("Kookmin University","kookmin"),
	KOOKJE("Kookje University", "kookje"), KUNSAN("Kunsan National University", "kunsan"), GANGSEO("Gangseo University", "kcu"), KDU("Far East University", "kdu"),
	GLOBAL("Global Cyber University", "global"), GGU("Geumgang University", "ggu"), KUMOH("Kumoh National Institute of Technology", "kumoh"),
	GCH("Gyeongbuk Collage of Health", "gch"), GIMCHEON("Gimcheon University", "gimcheon"), UKP("Kimpo University", "kimpo"),
	GIMHAE("Gimhae University", "gimhae"), KKOT("Catholic Kkottongnae University", "kkot"), KORNU("Korea Nazarene University", "kornu"),
	NAMBU("Nambu University", "nambu"), NSU("Namseoul University", "nsu"), NONGHYUP("Nonghyup University", "nonghyup"),
	DANKOOK("Dankook University","dankook"), TK("Daekyung University", "tk"), CU("Daegu Catholic University", "cu"),
	TTC("Daegu Technical University", "ttc"), TSU("Daegu Science University", "tsu"),
	DNUE("Daegu National University of Education", "dnue"), DAEGU("Daegu University", "daegu"), DHC("Daegu Health College", "dhc"),
	DCU("Daegu Cyber University", "dcu"), DGAU("Daegu Arts University", "dgau"), DAELIM("Daelim University College", "daelim"),
	DAESHIN("Daeshin University", "daeshin"), DAEWON("Daewon University", "daewon"),
	DJU("Daejeon University", "dju"), HIT("Daejeon Health College", "hit"), TMISSION("Daejeon Theological Seminary", "tmission"),
	DAEJIN("Daejin University", "daejin"), DUKSUNG("Duksung Women's University","duksung"), DKU("Dong-gang University", "dku"),
	DONGGUK("Dongguk University","dongguk.edu"), DONGGUK_WISE("Dongguk University (Gyeongju Campus)", "dongguk"),
	DONGNAM("Dongnam Health University College", "dongnam"), DONGDUK("Dongduk Women's University","dongduk"),
	TU("Dongmyung University", "tu"), DU("Dongseoul University", "du"), DSU("Dongshin University", "dsu"),
	DONGA("Dong-A University", "donga"), DIMA("Dong-Ah Institute of Media and Arts", "dima"), DUH("Dong-A University of Health", "duh"),
	DYU("Dongyang University", "dyu"), DONGYANG("Dongyang Mirae University", "dongyang"), DIT("Dongeui University College of Science and Technology", "dit"),
	DEU("Dongeui University", "deu"), DOOWAN("Doowon Technical University College", "doowon"), SCAU("Digital Seoul Culture Arts University", "scau"),
	MASAN("Masan University", "masan"), MJU("Myongji University","mju"), MJC("Myongji College","mjc"), MOKWON("Mokwon University", "mokwon"),
	MCU("Mokpo Catholic University", "mcu"), MNU("Mokpo National University", "mnu"),
	MMU("Mokpo National Maritime University", "mmu"), BAEWHA("Baewha Women's University", "baewha"),
	BU("Baekseok University", "bu"), CUP("Catholic University of Busan", "cup"), BIST("Busan University of Science and Technology", "bist"),
	PUSAN("Pusan National University", "pusan"), BDU("Busan Digital University", "bdu"), BWC("Busan Women's University", "bwc"),
	BUFS("Busan University of Foreign Studies", "bufs"), BPU("Busan Presbyterian Theological Seminary", "bpu"),
	BC("Bucheon University", "bc"), CUFS("Cyber Hankuk University of Foreign Studies", "cufs"), SYUIN("Sahmyook University", "syuin"), SHU("Sahmyook Health University College", "shu"),
	SANGMYUNG("Sangmyung University", "sangmyung"), SANGMYUNG_CHEONAN("Sangmyung University (Cheonan Campus)", "sangmyung"),
	SANGJI("Sangji University", "sangji"), SOGANG("Sogang University","sogang"), SKUNIV("Seokyeong University","skuniv"), SEOULTECH("Seoul National University of Science and Technology", "seoultech"),
	SNUE("Seoul National University of Education", "snue"), SCUI("Seoul Christian University", "scui"),  SNU("Seoul National University","snu"),
	EULJI("Eulji University", "eulji"), ISCU("Seoul Cyber University", "iscu"), UOS("University of Seoul","uos"), STU("Seoul Theological University", "stu"),
	SWU("Seoul Women's University","swu"), SEOULARTS("Seoul Institute of the Arts", "seoularts"), SJS("Seoul Jangsin University", "sjs"), SEOWON("Seowon University", "seowon"),
	SEOIL("Seoil University", "seoil"), SUNMOON("Sun Moon University", "sunmoon"), SUNGKYUL("Sungkyul University","sungkyul"),
	SKHU("Anglican University", "skhu"), SKKU("Sungkyunkwan University","skku"),  SUNGSHIN("Sungshin Women's University","sungshin"),
	SEMYUNG("Semyung University", "semyung"), SJU("Sejong University","sju"), SJCU("Sejong Cyber University", "sjcu"), SEHAN("Sehan University", "sehan"),
	SONGGOK("Songgok University", "songgok"), SONGWON("Songwon University", "songwon"), SONGHO("Songho University", "songho"), SC("Suseong University", "sc"),
	SUWONCATHOLIC("Suwon Catholic University", "suwoncatholic"), SSC("Suwon Science College", "ssc"), SUWON("University of Suwon", "suwon"), SWWU("Suwon Women's University", "swwu"),
	SOOK("Sookmyung Women's University","sookmyung"), KCC("Full Gospel Theological Seminary", "kcc"), SCNU("Sunchon National University", "scnu"), SUNCHEON("Sunchon First University", "suncheon"),
	SCH("Soonchunhyang University", "sch"), SSU("Soongsil University","soongsil"), KCU("Soongsil Cyber University", "kcu"),
	SEWU("Sungui Women's University", "sewc"), HSMU("Hwasung Medi-Science University", "hsmu"), SHINGU("Shingu University", "shingu"),
	SIlLA("Silla University", "sillain"), SHINSUNG("Sinsung University", "shinsung"), SAU("Shin Ansan University", "sau"),
	SHINHAN("Shinhan University", "shinhan"), AJOU("Ajou University","ajou"), MOTOR("Ajou Motor College", "motor"), ASC("Andong Science University", "asc"),
	ANU("Andong National Universitym", "anu"), ANSAN("Ansan University", "ansan"), ANYANG("Anyang University", "anyang"),
	YIT("Yeoju University", "yit"), YEONSUNG("Yeonseong University", "yeonsung"), YONSEI("Yonsei University","yonsei"), YONSEI_MIRAE("Yonsei University (Wonju Campus)", "yonsei"),
	YC("Yeonam Institute of Technology", "yc"), YU("Yeungnam University", "yu"), YTUS("Yeungnam Theological Seminary", "ytus"),
	YNC("Yeungnam University College of Engineering", "ync"), YOUNGSAN("Youngsan Seonhak University", "youngsan"), YCC("Yeongjin Cyber University", "ycc"),
	YJU("Yeongjin Professional University", "yju"), JESUS("Jesus University", "jesus"), YEWON("Yewon Arts University", "yewon"),
	OSAN("Osan University", "osan"), YIU("Yongin University", "yiu"), YSC("Yong-in Arts & Science University", "ysc"),
	WOOSUK("Woosuk University", "woosuk"), WSU("Woosong University", "wsu"), WOOSONG("Woosong University", "woosong"), WSI("Woosong Information University", "wsi"),
	UC("Ulsan College of Science", "uc"), ULSAN("University of Ulsan", "ulsan"), WAT("Ungji Tax University", "wat"), WKU("Wonkwang University", "wku"),
	WDU("Wonkwang Digital University", "wdu"), WU("Wonkwang Health Science College", "wu"), GWNU("Gangneung-Wonju National University", "gwnu"),
	UU("Wideok University","uu"), YD("Uwon University", "yd"), YUHAN("Yuhan University", "yuhan"),
	EWHA("Ewha Womans University","ewhain"), INDUK("Induk University", "induk"), INJE("Inje University", "inje"),
	ICCU("Incheon Catholic University", "iccu"), INU("Incheon National University","inu"),
	JEIU("JEI University", "jeiu"), ITC("Inha Technical College","itc"), INHA("Inha University","inha"),
	PUTS("Presbyterian Theological Seminary", "puts"), JANGAN("Jangan University", "jangan"), CNTU("Jeonnam Science University","cntu"),
	JNU("Chonnam National University","jnu"), DORIP("Jeonnam Provincial College", "dorip"), JBSC("Jeonbuk Science College", "jbsc"),
	JBNU("Jeonbuk National University", "jbnu"), JUNE("Jeonju National University of Education", "jnue"), KIJEON("Jeonju Kijeon University", "kijeon"),
	JJ("Jeonju University", "jj"), JVISION("Jeonju Vision University", "jvision"), JTU("Jeju Tourism University", "jtu"),
	JEJU("Jeju International University", "jeju"), JEJUNU("Jeju National University", "jejunu"), HALLA("Jeju Halla University", "halla"),
	CNC("Chosun Nursing College", "cnc"), CHOSUN("Chosun University", "chosun"), CST("Chosun College of Science and Technology", "chosun"),
	JOONGBU("Joongbu University", "jmail"), CAU("Chung-Ang University","cau"), CAU_ANSEONG("Chung-Ang University (Anseong Campus)", "cau"),
	SANGHA("Central Sangha University", "sangha"), JWU("Jungwon University", "jwu"), CUE("Jinju National University of Education", "cue"),
	JHC("Jinju Health College", "jhc"), GNU_CHILAM("Gyeongsang National University (Chilam Campus)", "gnu"),
	CHA("CHA University", "chauniv"), CS("Changshin University", "cs"), CHANGWON("Changshin University", "changwon"),
	CMU("Changwon Moonsung University", "cmu"), YONAM("Yonam University", "yonam"), CHUNGKANG("Chungkang College of Cultural Industries", "chungkang"), CK("Chungkang College of Cultural Industries", "ck"),
	CA("Cheongam University", "ca"), CHUNGWOON("Chungwoon University", "chungwoon"), CJE("Cheongju National University of Education", "cje"),
	CJU("Cheongju University", "cju"), CHUGYE("Chugye University for the Arts", "chugye"), CNUE("Chuncheon National University of Education", "cnue"),
	CH("Chunhae Health University College", "ch"), CNU("Chunhae Health University College", "cnu"), CNSU("Chungnam State University", "cnsu"),
	CHUNGBUK("Chungbuk National University","chungbuk"), CPU("Chungbuk Provincial University", "cpu"),
	CHSU("Chungbuk Health Science University College", "chsu"), OK("Chungcheong University", "ok"), KBTUS("Baptist Theological Seminary", "kbtus"),
	CALBIN("Calvin University", "calvin"), PTU("Pyeongtaek University", "ptu"), POHANG("Pohang University", "pohang"),
	HKNU("Hankyong National University", "hknu"), KTC("Korea Tourism University", "ktc"), KNUE("Korea National University of Education", "knue"),
	UT("Korea National University of Transportation", "ut"), AF("Korea National College of Agriculture and Fisheries", "af"),
	KOREATECH("Korea University of Technology and Education", "koreatech"), KNOU("Korea National Open University", "knou"),
	TUKOREA("Tech University of Korea", "tukorea"), BIBLE("Korea Bible University", "bible"), KLC("Korea Lift College", "klc"),
	PRO("Korea University of Media Arts", "pro"), KARTS("Korea National University of Arts", "karts"), HUFS("Hankuk University of Foreign Studies","hufs"),
	KNUH("Korea National University of Traditional Arts", "knuh"), KNSU("Korea National Sport University","knsu"),
	KOPO("Korea Polytechnics University", "kopo"), KAU("Korea Aerospace University", "kau"), KMOU("Korea Maritime & Ocean University", "kmou"),
	HANNAM("Hannam University", "hnu"), HANDONG("Handong Global University", "handong"), HALLYM("Hallym University", "hallym"),
	HSC("Hallym Sacred Heart University", "hsc"), HANBAT("Hanbat National University", "hanbat"), HANSEO("Hanseo University", "hanseo"),
	HANSUNG("Hansung University", "hansung"), HANSEI("Hansei University", "hansei"), HANSHIN("Hanshin University", "hanshin"),
	HANYANG("Hanyang University","hanyang"), ERICA("Hanyang University (ERICA Campus)","hanyang"), HYCU("Hanyang Cyber University", "hycu"),
	HYWOMAN("Hanyang Women's University", "hywoman"), HANYUNG("Hanyoung University", "hanyoung"),
	HANIL("Hanil Presbyterian Theological Seminary", "hanil"), UHS("Hyupsung University", "uhs"),
	HJ("Hyejeon University", "hj"), DST("Daejeon Institute of Science and Technology", "dst"), HONAM("Honam University", "honam"),
	HTUS("Honam Theological University", "htus"), HOSEO("Hoseo University", "hoseo"), HOWON("Howon University", "howon"),
	HONGIK("Hongik University","hongik"), HONGIK_SEJONG("Hongik University (Sejong Campus)", "hongik"),
	HSCU("Hwasein Cyber University", "hscu"),
	PKNU("Pukyong National University","pknu"), SDU("Seoul Digital University","sdu"), BSCU("Baekseok Culture University","bscu"),
	DONGSEO("Dongseo University","dongseo"), PCU("Pai Chai University","pcu"), SEOYOUNG("Seoyeong University","seoyoung"),
	TW("Dongwon University", "tw"),
	DGIST("DGIST (Daegu Gyeongbuk Institute of Science and Technology)","dgist"),
	GIST("GIST (Gwangju Institute of Science and Technology)","gist"),
	KAIST("KAIST (Korea Advanced Institute of Science and Technology)","kaist"),
	POSTECH("POSTECH (Pohang University of Science and Technology)","postech"),
	UNIST("UNIST (Ulsan National Institute of Science and Technology)","unist");

	private static final Map<String, List<UnivMail>> UNIV_MAIL_MAP = Collections.unmodifiableMap(
		Stream.of(values())
			.collect(Collectors.groupingBy(UnivMail::getUnivCode))
	);

	private final String univCode;
	private final String domain;

	public static boolean exists(String univCode) {
		return UNIV_MAIL_MAP.containsKey(univCode);
	}

	public static void validateUnivCode(String univCode) {
		boolean isExist = exists(univCode);
		if(!isExist)
			throw new AppException(ErrorCode.UNIVERSITY_EMAIL_NOT_SUPPORTED);
	}

	public static List<String> getDomains(String univCode) {
		List<UnivMail> list = UNIV_MAIL_MAP.get(univCode);
		return list.stream()
			.map(UnivMail::getDomain)
			.toList();
	}
}
