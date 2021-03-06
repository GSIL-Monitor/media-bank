package com.syswin.temail.media.bank.utils;

import java.util.HashMap;
import java.util.Map;

public class HttpContentTypeUtils {

	private static String getSuff(String s) {
		if (s == null || s.length() <= 1)
			return ".*";

		int p = s.lastIndexOf('.');
		if (p == -1)
			return ".*";

		return s.substring(s.lastIndexOf('.'));
	}

	
	public static String getMineType(String s) {
		String t = m.get(getSuff(s));
          if(t == null) return "application/octet-stream";
		return t;
	}

	private static Map<String, String> m = new HashMap<String, String>();
	 
	static {
		m.put(".css","text/css");
		m.put(".m3u","audio/mpegurl");
		m.put(".cdf","application/x-netcdf");
		m.put(".ptn","application/x-ptn");
		m.put(".acp","audio/x-mei-aac");
		m.put(".asf","video/x-ms-asf");
		m.put(".x_b","application/x-x_b");
		m.put(".gbr","application/x-gbr");
		m.put(".lbm","application/x-lbm");
		m.put(".asa","text/asa");
		m.put(".ra","audio/vnd.rn-realaudio");
		m.put(".mp2v","video/mpeg");
		m.put(".hgl","application/x-hgl");
		m.put(".lavs","audio/x-liquid-secure");
		m.put(".xpl","audio/scpls");
		m.put(".m4e","video/mpeg4");
		m.put(".slk","drawing/x-slk");
		m.put(".asx","video/x-ms-asf");
		m.put(".au","audio/basic");
		m.put(".x_t","application/x-x_t");
		m.put(".mpga","audio/rn-mpeg");
		m.put(".etd","application/x-ebx");
		m.put(".dwf","application/x-dwf");
		m.put(".wma","audio/x-ms-wma");
		m.put(".asp","text/asp");
		m.put(".xfdf","application/vnd.adobe.xfdf");
		m.put(".rmvb","application/vnd.rn-realmedia-vbr");
		m.put(".csi","application/x-csi");
		m.put(".dwg","application/x-dwg");
		m.put(".wmf","application/x-wmf");
		m.put(".rt","text/vnd.rn-realtext");
		m.put(".dgn","application/x-dgn");
		m.put(".ai","application/postscript");
		m.put(".wmd","application/x-ms-wmd");
		m.put(".iii","application/x-iphone");
		m.put(".m2v","video/x-mpeg");
		m.put(".rv","video/vnd.rn-realvideo");
		m.put(".pcx","application/x-pcx");
		m.put(".wml","text/vnd.wap.wml");
		m.put(".","application/x-");
		m.put(".snd","audio/basic");
		m.put(".wmv","video/x-ms-wmv");
		m.put(".xql","text/xml");
		m.put(".prt","application/x-prt");
		m.put(".smi","application/smil");
		m.put(".wmz","application/x-ms-wmz");
		m.put(".smk","application/x-smk");
		m.put(".mdb","application/x-mdb");
		m.put(".wmx","video/x-ms-wmx");
		m.put(".pci","application/x-pci");
		m.put(".rm","application/vnd.rn-realmedia");
		m.put(".pcl","application/x-pcl");
		m.put(".dxb","application/x-dxb");
		m.put(".323","text/h323");
		m.put(".rp","image/vnd.rn-realpix");
		m.put(".dxf","application/x-dxf");
		m.put(".m1v","video/x-mpeg");
		m.put(".midi","audio/mid");
		m.put(".cut","application/x-cut");
		m.put(".c90","application/x-c90");
		m.put(".prf","application/pics-rules");
		m.put(".mpeg","video/mpg");
		m.put(".xsd","text/xml");
		m.put(".prn","application/x-prn");
		m.put(".tsd","text/xml");
		m.put(".rjt","application/vnd.rn-realsystem-rjt");
		m.put(".rjs","application/vnd.rn-realsystem-rjs");
		m.put(".r3t","text/vnd.rn-realtext3d");
		m.put(".cg4","application/x-g4");
		m.put(".cer","application/x-x509-ca-cert");
		m.put(".ltr","application/x-ltr");
		m.put(".igs","application/x-igs");
		m.put(".der","application/x-x509-ca-cert");
		m.put(".edn","application/vnd.adobe.edn");
		m.put(".pc5","application/x-pc5");
		m.put(".rle","application/x-rle");
		m.put(".ps","application/x-ps");
		m.put(".pr","application/x-pr");
		m.put(".rlc","application/x-rlc");
		m.put(".cel","application/x-cel");
		m.put(".wkq","application/x-wkq");
		m.put(".slb","application/x-slb");
		m.put(".avi","video/avi");
		m.put(".sld","application/x-sld");
		m.put(".lar","application/x-laplayer-reg");
		m.put(".xsl","text/xml");
		m.put(".torrent","application/x-bittorrent");
		m.put(".ppt","application/vnd.ms-powerpoint");
		m.put(".pps","application/vnd.ms-powerpoint");
		m.put(".wks","application/x-wks");
		m.put(".cdr","application/x-cdr");
		m.put(".latex","application/x-latex");
		m.put(".mxp","application/x-mmxp");
		m.put(".pl","application/x-perl");
		m.put(".301","application/x-301");
		m.put(".dll","application/x-msdownload");
		m.put(".sat","application/x-sat");
		m.put(".wr1","application/x-wr1");
		m.put(".ppm","application/x-ppm");
		m.put(".epi","application/x-epi");
		m.put(".mpv2","video/mpeg");
		m.put(".xlw","application/x-xlw");
		m.put(".sam","application/x-sam");
		m.put(".xhtml","application/octet-stream");
		m.put(".wpg","application/x-wpg");
		m.put(".wpl","application/vnd.ms-wpl");
		m.put(".xls","application/vnd.ms-excel");
		m.put(".rmf","application/vnd.adobe.rmf");
		m.put(".ppa","application/vnd.ms-powerpoint");
		m.put(".eps","application/postscript");
		m.put(".wbmp","image/vnd.wap.wbmp");
		m.put(".mi","application/x-mi");
		m.put(".spp","text/xml");
		m.put(".pot","application/vnd.ms-powerpoint");
		m.put(".spl","application/futuresplash");
		m.put(".vml","text/xml");
		m.put(".la1","audio/x-liquid-file");
		m.put(".cgm","application/x-cgm");
		m.put(".ws2","application/x-ws");
		m.put(".img","application/x-img");
		m.put(".fdf","application/vnd.fdf");
		m.put(".apk","application/vnd.android.package-archive");
		m.put(".rmi","audio/mid");
		m.put(".rmj","application/vnd.rn-realsystem-rmj");
		m.put(".rmm","audio/x-pn-realaudio");
		m.put(".rmp","application/vnd.rn-rn_music_package");
		m.put(".xml","text/xml");
		m.put(".rms","application/vnd.rn-realmedia-secure");
		m.put(".rmx","application/vnd.rn-realsystem-rmx");
		m.put(".p10","application/pkcs10");
		m.put(".wp6","application/x-wp6");
		m.put(".p12","application/x-pkcs12");
		m.put(".png","image/png");
		m.put(".xquery","text/xml");
		m.put(".jpg","image/jpeg");
		m.put(".jpe","application/x-jpe");
		m.put(".rsml","application/vnd.rn-rsml");
		m.put(".frm","application/x-frm");
		m.put(".xap","application/x-silverlight-app");
		m.put(".aiff","audio/aiff");
		m.put(".aifc","audio/aiff");
		m.put(".rnx","application/vnd.rn-realplayer");
		m.put(".gif","image/gif");
		m.put(".wav","audio/wav");
		m.put(".cit","application/x-cit");
		m.put(".spc","application/x-pkcs7-certificates");
		m.put(".wq1","application/x-wq1");
		m.put(".mac","application/x-mac");
		m.put(".ls","application/x-javascript");
		m.put(".IVF","video/x-ivf");
		m.put(".top","drawing/x-top");
		m.put(".man","application/x-troff-man");
		m.put(".plt","application/x-plt");
		m.put(".sor","text/plain");
		m.put(".pls","audio/scpls");
		m.put(".gl2","application/x-gl2");
		m.put(".wpd","application/x-wpd");
		m.put(".c4t","application/x-c4t");
		m.put(".fax","image/fax");
		m.put(".sol","text/plain");
		m.put(".wb1","application/x-wb1");
		m.put(".wb2","application/x-wb2");
		m.put(".vpg","application/x-vpeg005");
		m.put(".sisx","application/vnd.symbian.install");
		m.put(".wb3","application/x-wb3");
		m.put(".dib","application/x-dib");
		m.put(".pko","application/vnd.ms-pki.pko");
		m.put(".doc","application/msword");
		m.put(".html","application/octet-stream");
		m.put(".rpm","audio/x-pn-realaudio-plugin");
		m.put(".bot","application/x-bot");
		m.put(".sty","application/x-sty");
		m.put(".js","application/octet-stream");
		m.put(".dot","application/msword");
		m.put(".txt","text/plain");
		m.put(".tif","application/x-tif");
		m.put(".ras","application/x-ras");
		m.put(".rat","application/rat-file");
		m.put(".mps","video/x-mpeg");
		m.put(".mpt","application/vnd.ms-project");
		m.put(".plg","text/html");
		m.put(".mpv","video/mpg");
		m.put(".ram","audio/x-pn-realaudio");
		m.put(".mpw","application/vnd.ms-project");
		m.put(".mpx","application/vnd.ms-project");
		m.put(".a11","application/x-a11");
		m.put(".nrf","application/x-nrf");
		m.put(".smil","application/smil");
		m.put(".hpl","application/x-hpl");
		m.put(".movie","video/x-sgi-movie");
		m.put(".hpg","application/x-hpgl");
		m.put(".svg","text/xml");
		m.put(".sdp","application/sdp");
		m.put(".uls","text/iuls");
		m.put(".sdw","application/x-sdw");
		m.put(".wm","video/x-ms-wm");
		m.put(".lmsff","audio/x-la-lms");
		m.put(".bmp","application/x-bmp");
		m.put(".ipa","application/vnd.iphone");
		m.put(".mml","text/xml");
		m.put(".hmr","application/x-hmr");
		m.put(".tld","text/xml");
		m.put(".ws","application/x-ws");
		m.put(".vsw","application/vnd.visio");
		m.put(".vsx","application/vnd.visio");
		m.put(".vss","application/vnd.visio");
		m.put(".vst","application/x-vst");
		m.put(".wax","audio/x-ms-wax");
		m.put(".cml","text/xml");
		m.put(".mns","audio/x-musicnet-stream");
		m.put(".jpeg","image/jpeg");
		m.put(".eml","message/rfc822");
		m.put(".cmp","application/x-cmp");
		m.put(".mp1","audio/mp1");
		m.put(".emf","application/x-emf");
		m.put(".mp2","audio/mp2");
		m.put(".mp3","audio/mp3");
		m.put(".mp4","video/mp4");
		m.put(".ssm","application/streamingmedia");
		m.put(".mpp","application/vnd.ms-project");
		m.put(".cmx","application/x-cmx");
		m.put(".xq","text/xml");
		m.put(".anv","application/x-anv");
		m.put(".xslt","text/xml");
		m.put(".sst","application/vnd.ms-pki.certstore");
		m.put(".vsd","application/vnd.visio");
		m.put(".mnd","audio/x-musicnet-download");
		m.put(".mpg","video/mpg");
		m.put(".wsc","text/scriptlet");
		m.put(".gp4","application/x-gp4");
		m.put(".mpe","video/x-mpeg");
		m.put(".mpd","application/vnd.ms-project");
		m.put(".mpa","video/x-mpg");
		m.put(".ins","application/x-internet-signup");
		m.put(".wrk","application/x-wrk");
		m.put(".wri","application/x-wri");
		m.put(".ent","text/xml");
		m.put(".vcf","text/x-vcard");
		m.put(".odc","text/x-ms-odc");
		m.put(".mhtml","message/rfc822");
		m.put(".rtf","application/x-rtf");
		m.put(".jsp","text/html");
		m.put(".stm","text/html");
		m.put(".pic","application/x-pic");
		m.put(".001","application/x-001");
		m.put(".jfif","image/jpeg");
		m.put(".stl","application/vnd.ms-pki.stl");
		m.put(".906","application/x-906");
		m.put(".p7r","application/x-pkcs7-certreqresp");
		m.put(".p7s","application/pkcs7-signature");
		m.put(".exe","application/x-msdownload");
		m.put(".907","drawing/907");
		m.put(".rec","application/vnd.rn-recording");
		m.put(".p7m","application/pkcs7-mime");
		m.put(".red","application/x-red");
		m.put(".sis","application/vnd.symbian.install");
		m.put(".pgl","application/x-pgl");
		m.put(".iff","application/x-iff");
		m.put(".vdx","application/vnd.visio");
		m.put(".tg4","application/x-tg4");
		m.put(".dcx","application/x-dcx");
		m.put(".p7b","application/x-pkcs7-certificates");
		m.put(".vda","application/x-vda");
		m.put(".mtx","text/xml");
		m.put(".p7c","application/pkcs7-mime");
		m.put(".cot","application/x-cot");
		m.put(".xdr","text/xml");
		m.put(".fo","text/xml");
		m.put(".xdp","application/vnd.adobe.xdp");
		m.put(".mfp","application/x-shockwave-flash");
		m.put(".sit","application/x-stuffit");
		m.put(".ig4","application/x-g4");
		m.put(".uin","application/x-icq");
		m.put(".rdf","text/xml");
		m.put(".wiz","application/msword");
		m.put(".htm","text/html");
		m.put(".dtd","text/xml");
		m.put(".dbm","application/x-dbm");
		m.put(".htx","text/html");
		m.put(".wk3","application/x-wk3");
		m.put(".dbx","application/x-dbx");
		m.put(".out","application/x-out");
		m.put(".wk4","application/x-wk4");
		m.put(".htt","text/webviewhtml");
		m.put(".tiff","image/tiff");
		m.put(".hta","application/hta");
		m.put(".dcd","text/xml");
		m.put(".class","java/*");
		m.put(".tdf","application/x-tdf");
		m.put(".g4","application/x-g4");
		m.put(".pfx","application/x-pkcs12");
		m.put(".vxml","text/xml");
		m.put(".htc","text/x-component");
		m.put(".vtx","application/vnd.visio");
		m.put(".aif","audio/aiff");
		m.put(".xfd","application/vnd.adobe.xfd");
		m.put(".math","text/xml");
		m.put(".nws","message/rfc822");
		m.put(".cat","application/vnd.ms-pki.seccat");
		m.put(".mid","audio/mid");
		m.put(".cal","application/x-cals");
		m.put(".ico","application/x-ico");
		m.put(".tga","application/x-tga");
		m.put(".dbf","application/x-dbf");
		m.put(".mht","message/rfc822");
		m.put(".net","image/pnetvue");
		m.put(".fif","application/fractals");
		m.put(".swf","application/x-shockwave-flash");
		m.put(".mocha","application/x-javascript");
		m.put(".wsdl","text/xml");
		m.put(".crl","application/pkix-crl");
		m.put(".pwz","application/vnd.ms-powerpoint");
		m.put(".pdf","application/pdf");
		m.put(".*","application/octet-stream");
		m.put(".icb","application/x-icb");
		m.put(".wvx","video/x-ms-wvx");
		m.put(".hqx","application/mac-binhex40");
		m.put(".mil","application/x-mil");
		m.put(".xwd","application/x-xwd");
		m.put(".biz","text/xml");
		m.put(".awf","application/vnd.adobe.workflow");
		m.put(".java","java/*");
		m.put(".hrf","application/x-hrf");
		m.put(".rgb","application/x-rgb");
		m.put(".drw","application/x-drw");
		m.put(".crt","application/x-x509-ca-cert");
		m.put(".pdx","application/vnd.adobe.pdx");
		m.put(".isp","application/x-internet-signup");
		m.put(".pko","application/ynd.ms-pkipko");
		m.put(".doc","application/msword");
		m.put(".css","text/css");
		m.put(".qt","video/quicktime");
		m.put(".acx","application/internet-property-stream");
		m.put(".m3u","audio/x-mpegurl");
		m.put(".cdf","application/x-cdf");
		m.put(".html","text/html");
		m.put(".etx","text/x-setext");
		m.put(".asf","video/x-ms-asf");
		m.put(".roff","application/x-troff");
		m.put(".oda","application/oda");
		m.put(".ra","audio/x-pn-realaudio");
		m.put(".js","application/x-javascript");
		m.put(".lzh","application/octet-stream");
		m.put(".dot","application/msword");
		m.put(".xpm","image/x-xpixmap");
		m.put(".txt","text/plain");
		m.put(".tar","application/x-tar");
		m.put(".pot,","application/vnd.ms-powerpoint");
		m.put(".wdb","application/vnd.ms-works");
		m.put(".tif","image/tiff");
		m.put(".asx","video/x-ms-asf");
		m.put(".au","audio/basic");
		m.put(".ras","image/x-cmu-raster");
		m.put(".ram","audio/x-pn-realaudio");
		m.put(".pbm","image/x-portable-bitmap");
		m.put(".csh","application/x-csh");
		m.put(".asr","video/x-ms-asf");
		m.put(".wmf","application/x-msmetafile");
		m.put(".ai","application/postscript");
		m.put(".iii","application/x-iphone");
		m.put(".dxr","application/x-director");
		m.put(".movie","video/x-sgi-movie");
		m.put(".sv4cpio","application/x-sv4cpio");
		m.put(".snd","audio/basic");
		m.put(".svg","image/svg+xml");
		m.put(".uls","text/iuls");
		m.put(".mdb","application/x-msaccess");
		m.put(".bcpio","application/x-bcpio");
		m.put(".323","text/h323");
		m.put(".clp","application/x-msclip");
		m.put(".bmp","image/bmp");
		m.put(".scd","application/x-msschedule");
		m.put(".evy","application/envoy");
		m.put(".gz","application/x-gzip");
		m.put(".prf","application/pics-rules");
		m.put(".trm","application/x-msterminal");
		m.put(".mpeg","video/mpeg");
		m.put(".cpio","application/x-cpio");
		m.put(".lsx","video/x-la-asf");
		m.put(".tcl","application/x-tcl");
		m.put(".sct","text/scriptlet");
		m.put(".dms","application/octet-stream");
		m.put(".cer","application/x-x509-ca-cert");
		m.put(".shar","application/x-shar");
		m.put(".mny","application/x-msmoney");
		m.put(".der","application/x-x509-ca-cert");
		m.put(".jpeg","image/jpeg");
		m.put(".mp2","video/mpeg");
		m.put(".mp3","audio/mpeg");
		m.put(".z","application/x-compress");
		m.put(".gtar","application/x-gtar");
		m.put(".mpp","application/vnd.ms-project");
		m.put(".cmx","image/x-cmx");
		m.put(".wrz","x-world/x-vrml");
		m.put(".ustar","application/x-ustar");
		m.put(".ps","application/postscript");
		m.put(".sst","application/vnd.ms-pkicertstore");
		m.put(".wcm","application/vnd.ms-works");
		m.put(".mpg","video/mpeg");
		m.put(".t","application/x-troff");
		m.put(".mpe","video/mpeg");
		m.put(".avi","video/x-msvideo");
		m.put(".tsv","text/tab-separated-values");
		m.put(".texi","application/x-texinfo");
		m.put(".ins","application/x-internet-signup");
		m.put(".mpa","video/mpeg");
		m.put(".wrl","x-world/x-vrml");
		m.put(".setpay","application/set-payment-initiation");
		m.put(".wri","application/x-mswrite");
		m.put(".vcf","text/x-vcard");
		m.put(".ppt","application/vnd.ms-powerpoint");
		m.put(".mhtml","message/rfc822");
		m.put(".wks","application/vnd.ms-works");
		m.put(".pps","application/vnd.ms-powerpoint");
		m.put(".dvi","application/x-dvi");
		m.put(".rtf","application/rtf");
		m.put(".latex","application/x-latex");
		m.put(".lsf","video/x-la-asf");
		m.put(".mov","video/quicktime");
		m.put(".stm","text/html");
		m.put(".jfif","image/pipeg");
		m.put(".xla","application/vnd.ms-excel");
		m.put(".stl","application/vnd.ms-pkistl");
		m.put(".p7r","application/x-pkcs7-certreqresp");
		m.put(".dll","application/x-msdownload");
		m.put(".dcr","application/x-director");
		m.put(".ppm","image/x-portable-pixmap");
		m.put(".p7s","application/x-pkcs7-signature");
		m.put(".exe","application/octet-stream");
		m.put(".p7m","application/x-pkcs7-mime");
		m.put(".cod","image/cis-cod");
		m.put(".wps","application/vnd.ms-works");
		m.put(".pgm","image/x-portable-graymap");
		m.put(".mpv2","video/mpeg");
		m.put(".tex","application/x-tex");
		m.put(".xlw","application/vnd.ms-excel");
		m.put(".rtx","text/richtext");
		m.put(".m14","application/x-msmediaview");
		m.put(".m13","application/x-msmediaview");
		m.put(".pub","application/x-mspublisher");
		m.put(".xls","application/vnd.ms-excel");
		m.put(".ms","application/x-troff-ms");
		m.put(".xlt","application/vnd.ms-excel");
		m.put(".eps","application/postscript");
		m.put(".p7b","application/x-pkcs7-certificates");
		m.put(".p7c","application/x-pkcs7-mime");
		m.put(".xlm","application/vnd.ms-excel");
		m.put(".sv4crc","application/x-sv4crc");
		m.put(".sit","application/x-stuffit");
		m.put(".xlc","application/vnd.ms-excel");
		m.put(".me","application/x-troff-me");
		m.put(".spl","application/futuresplash");
		m.put(".c","text/plain");
		m.put(".htm","text/html");
		m.put(".src","application/x-wais-source");
		m.put(".lha","application/octet-stream");
		m.put(".htt","text/webviewhtml");
		m.put(".ief","image/ief");
		m.put(".h","text/plain");
		m.put(".tiff","image/tiff");
		m.put(".rmi","audio/mid");
		m.put(".hta","application/hta");
		m.put(".flr","x-world/x-vrml");
		m.put(".class","application/octet-stream");
		m.put(".mvb","application/x-msmediaview");
		m.put(".xbm","image/x-xbitmap");
		m.put(".pfx","application/x-pkcs12");
		m.put(".hlp","application/winhlp");
		m.put(".htc","text/x-component");
		m.put(".pnm","image/x-portable-anymap");
		m.put(".p10","application/pkcs10");
		m.put(".p12","application/x-pkcs12");
		m.put(".aif","audio/x-aiff");
		m.put(".tgz","application/x-compressed");
		m.put(".xaf","x-world/x-vrml");
		m.put(".cat","application/vnd.ms-pkiseccat");
		m.put(".nws","message/rfc822");
		m.put(".hdf","application/x-hdf");
		m.put(".bin","application/octet-stream");
		m.put(".jpg","image/jpeg");
		m.put(".zip","application/zip");
		m.put(".jpe","image/jpeg");
		m.put(".mid","audio/mid");
		m.put(".bas","text/plain");
		m.put(".sh","application/x-sh");
		m.put(".ico","image/x-icon");
		m.put(".aiff","audio/x-aiff");
		m.put(".pmw","application/x-perfmon");
		m.put(".aifc","audio/x-aiff");
		m.put(".axs","application/olescript");
		m.put(".wav","audio/x-wav");
		m.put(".gif","image/gif");
		m.put(".pml","application/x-perfmon");
		m.put(".fif","application/fractals");
		m.put(".mht","message/rfc822");
		m.put(".texinfo","application/x-texinfo");
		m.put(".swf","application/x-shockwave-flash");
		m.put(".pmr","application/x-perfmon");
		m.put(".spc","application/x-pkcs7-certificates");
		m.put(".tr","application/x-troff");
		m.put(".pmc","application/x-perfmon");
		m.put(".crl","application/pkix-crl");
		m.put(".man","application/x-troff-man");
		m.put(".pdf","application/pdf");
		m.put(".pma","application/x-perfmon");
		m.put(".dir","application/x-director");
		m.put(".crd","application/x-mscardfile");
		m.put(".hqx","application/mac-binhex40");
		m.put(".vrml","x-world/x-vrml");
		m.put(".setreg","application/set-registration-initiation");
		m.put(".xwd","image/x-xwindowdump");
		m.put(".xof","x-world/x-vrml");
		m.put(".rgb","image/x-rgb");
		m.put(".crt","application/x-x509-ca-cert");
		m.put(".isp","application/x-internet-signup");
		m.put(".webp","image/webp");
	}

}