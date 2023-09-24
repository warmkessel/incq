package com.incq.constants;

import java.util.HashMap;
import java.util.Map;

public enum Language {
	ENGLISH("en", "English", "&#127482;&#127480;", " Reviews on all kinds of products", "Link to our Home Page", "Home",
			"Author", "Authors", "Contact Us", "Review", "Reviews", "INCQ All rights reserved",
			"As an Amazon Associate we earn from qualifying purchases.", "Login/Register", "Welcome", "Category:",
			"Our most popular:", "Here are some of our Authors", "Introduction", "Conclusion", "Check Amazon!",
			"We'd love to hear your thoughts. Kindly login so we can receive your message.", "Subject",
			"Your comment or question", "Share your thoughts", "We appreciate your interest. Check our ",
			"Privacy Policy", "Here are our most recognized authors", "More..."),
	ARABIC("ar", "Arabic", "&#127462;&#127487;", "مراجعات على جميع أنواع المنتجات", "رابط إلى الصفحة الرئيسية لدينا",
			"الرئيسية", "المؤلف", "المؤلفون", "اتصل بنا", "مراجعة", "مراجعات", "جميع الحقوق محفوظة لـ INCQ",
			"كشريك في Amazon نحصل على عمولات من المشتريات المؤهلة.", "تسجيل الدخول/تسجيل", "مرحبا", "الفئة:",
			"الأكثر شعبية لدينا:", "هؤلاء بعض المؤلفين لدينا", "مقدمة", "ختام", "تحقق من Amazon!",
			"نحن نرحب بآرائكم. يرجى تسجيل الدخول حتى نتمكن من تلقي رسالتكم.", "الموضوع", "تعليقك أو سؤالك",
			"شارك أفكارك", "نقدر اهتمامك. تحقق من ", "سياسة الخصوصية", "هؤلاء هم أشهر المؤلفين لدينا", "المزيد..."),
	MANDARIN_CHINESE("zh", "Mandarin Chinese", "&#127464;&#127475;", "各类产品的评价", "链接到我们的首页", "首页", "作者", "作者们", "联系我们",
			"评测", "评论", "INCQ 版权所有", "作为亚马逊合作伙伴，我们将从购买中获得收入。", "登录/注册", "欢迎", "分类：", "我们最受欢迎的：", "这里是我们的一些作者", "介绍",
			"总结", "查看亚马逊！", "我们很乐意听到您的想法。请登录，以便我们可以收到您的消息。", "主题", "您的评论或问题", "分享您的想法", "我们欣赏您的关注。查看我们的", "隐私政策",
			"这是我们最受认可的作者", "更多..."),
	DANISH("da", "Danish", "&#127465;&#127472;", "Anmeldelser af alle slags produkter", "Link til vores hjemmeside",
			"Hjem", "Forfatter", "Forfattere", "Kontakt os", "Anmeldelse", "Anmeldelser",
			"INCQ Alle rettigheder forbeholdes", "Som Amazon Associate tjener vi fra kvalificerede køb.",
			"Login/Register", "Velkommen", "Kategori:", "Vores mest populære:", "Her er nogle af vores forfattere",
			"Introduktion", "Konklusion", "Tjek Amazon!",
			"Vi vil gerne høre dine tanker. Log venligst ind, så vi kan modtage din besked.", "Emne",
			"Din kommentar eller spørgsmål", "Del dine tanker", "Vi sætter pris på din interesse. Tjek vores ",
			"Privatlivspolitik", "Her er vores mest anerkendte forfattere", "Mere..."),
	DUTCH("nl", "Dutch", "&#127470;&#127475;", "Recensies over allerlei producten", "Link naar onze homepage", "Thuis",
			"Auteur", "Auteurs", "Contact met ons opnemen", "Recensie", "Recensies", "INCQ Alle rechten voorbehouden",
			"Als Amazon Associate verdienen we aan in aanmerking komende aankopen.", "Inloggen/Registreren", "Welkom",
			"Categorie:", "Onze meest populaire:", "Hier zijn enkele van onze auteurs", "Introductie", "Conclusie",
			"Bekijk Amazon!", "We horen graag uw gedachten. Log alstublieft in zodat we uw bericht kunnen ontvangen.",
			"Onderwerp", "Uw opmerking of vraag", "Deel uw gedachten", "We waarderen uw interesse. Bekijk onze ",
			"Privacybeleid", "Hier zijn onze meest erkende auteurs", "Meer..."),
	FRENCH("fr", "French", "&#127467;&#127479;", "Avis sur toutes sortes de produits", "Lien vers notre page d'accueil",
			"Accueil", "Auteur", "Auteurs", "Contactez-nous", "Avis", "Commentaires", "INCQ Tous droits réservés",
			"En tant que Partenaire Amazon, nous réalisons un bénéfice sur les achats remplissant les conditions requises.",
			"Connexion/Inscription", "Bienvenue", "Catégorie:", "Nos plus populaires:",
			"Voici quelques-uns de nos auteurs", "Introduction", "Conclusion", "Vérifiez sur Amazon!",
			"Nous aimerions avoir votre avis. Veuillez vous connecter afin que nous puissions recevoir votre message.",
			"Sujet", "Votre commentaire ou question", "Partagez vos pensées",
			"Nous apprécions votre intérêt. Consultez notre ", "Politique de confidentialité",
			"Voici nos auteurs les plus reconnus", "Plus..."),
	GERMAN("de", "German", "&#127465;&#127466;", "Bewertungen zu allen Arten von Produkten",
			"Link zu unserer Startseite", "Start", "Autor", "Autoren", "Kontaktieren Sie uns", "Bewertung",
			"Bewertungen", "INCQ Alle Rechte vorbehalten", "Als Amazon-Partner verdienen wir an qualifizierten Käufen.",
			"Anmelden/Registrieren", "Willkommen", "Kategorie:", "Unsere beliebtesten:",
			"Hier sind einige unserer Autoren", "Einführung", "Fazit", "Prüfen Sie Amazon!",
			"Wir würden gerne Ihre Meinung hören. Bitte melden Sie sich an, damit wir Ihre Nachricht erhalten können.",
			"Betreff", "Ihr Kommentar oder Ihre Frage", "Teilen Sie Ihre Gedanken",
			"Wir schätzen Ihr Interesse. Überprüfen Sie unsere ", "Datenschutzrichtlinie",
			"Hier sind unsere anerkanntesten Autoren", "Mehr..."),
	HINDI("hi", "Hindi", "&#127470;&#127475;", "सभी प्रकार के उत्पादों पर समीक्षाएं", "हमारे होम पेज के लिए लिंक",
			"होम", "लेखक", "लेखक", "हमसे संपर्क करें", "समीक्षा", "समीक्षाएं", "INCQ सभी अधिकार सुरक्षित",
			"एमेज़ॉन एसोसिएट के रूप में हम योग्य खरीददारियों से कमाई करते हैं।", "लॉगिन/रजिस्टर", "स्वागत है",
			"श्रेणी:", "हमारे सबसे लोकप्रिय:", "यहाँ हमारे कुछ लेखक हैं", "परिचय", "निष्कर्ष", "अमेज़न जाँचें!",
			"हम आपके विचार सुनना पसंद करेंगे। कृपया लॉगिन करें ताकि हम आपका संदेश प्राप्त कर सकें।", "विषय",
			"आपकी टिप्पणी या प्रश्न", "अपने विचार साझा करें", "हम आपकी रुचि को सराहते हैं। हमारी जाँच करें ",
			"गोपनीयता नीति", "यहाँ हमारे सबसे पहचाने गए लेखक हैं", "और भी..."),
	ITALIAN("it", "Italian", "&#127470;&#127481;", "Recensioni su tutti i tipi di prodotti",
			"Link alla nostra Home Page", "Home", "Autore", "Autori", "Contattaci", "Recensione", "Recensioni",
			"INCQ Tutti i diritti riservati", "Come Associato Amazon guadagniamo dagli acquisti idonei.",
			"Login/Registrati", "Benvenuto", "Categoria:", "I più popolari:", "Ecco alcuni dei nostri autori",
			"Introduzione", "Conclusione", "Controlla su Amazon!",
			"Ci piacerebbe sentire i tuoi pensieri. Effettua il login così possiamo ricevere il tuo messaggio.",
			"Oggetto", "Il tuo commento o domanda", "Condividi i tuoi pensieri",
			"Apprezziamo il tuo interesse. Controlla il nostro ", "Politica sulla privacy",
			"Ecco i nostri autori più riconosciuti", "Altro..."),
	JAPANESE("ja", "Japanese", "&#127471;&#127477;", "あらゆる種類の製品のレビュー", "ホームページへのリンク", "ホーム", "著者", "著者たち", "お問い合わせ",
			"レビュー", "レビュー", "INCQ すべての権利を保有", "Amazonアソシエイトのメンバーとして、資格のある購入から収益を得ています。", "ログイン/登録", "ようこそ", "カテゴリー:",
			"一番人気:", "こちらが私たちの著者の一部です", "イントロダクション", "結論", "Amazonをチェック!", "あなたの意見をお聞かせください。メッセージを受け取るためにログインしてください。",
			"件名", "あなたのコメントまたは質問", "思いを共有してください", "あなたの興味に感謝します。私たちのをチェックしてください ", "プライバシーポリシー", "こちらが最も認知されている著者たちです",
			"もっと..."),
	KOREAN("ko", "Korean", "&#127472;&#127479;", "모든 종류의 제품 리뷰", "홈페이지 링크", "홈", "저자", "저자들", "문의하기", "리뷰", "리뷰들",
			"INCQ 모든 권리 보유", "Amazon Associate로서 자격이 있는 구매로부터 수익을 얻습니다.", "로그인/등록", "환영합니다", "카테고리:", "가장 인기 있는:",
			"저희의 몇몇 저자들입니다", "서론", "결론", "Amazon 확인!", "귀하의 의견을 듣고 싶습니다. 메시지를 받을 수 있도록 로그인해 주세요.", "제목",
			"귀하의 코멘트 또는 질문", "생각을 공유해주세요", "귀하의 관심에 감사드립니다. 확인해 보세요 ", "개인정보처리방침", "가장 인정받는 저자들입니다", "더보기..."),
	NORWEGIAN("no", "Norwegian", "&#127475;&#127487;", "Anmeldelser av alle slags produkter", "Link til vår hjemmeside",
			"Hjem", "Forfatter", "Forfattere", "Kontakt Oss", "Anmeldelse", "Anmeldelser",
			"INCQ Alle rettigheter reservert", "Som en Amazon Associate tjener vi fra kvalifiserende kjøp.",
			"Logg inn/Registrer deg", "Velkommen", "Kategori:", "Våre mest populære:", "Her er noen av våre forfattere",
			"Introduksjon", "Konklusjon", "Sjekk Amazon!",
			"Vi vil gjerne høre dine tanker. Vennligst logg inn slik at vi kan motta meldingen din.", "Emne",
			"Din kommentar eller spørsmål", "Del dine tanker", "Vi setter pris på din interesse. Sjekk vår ",
			"Personvernpolicy", "Her er våre mest anerkjente forfattere", "Mer..."),
	PORTUGUESE("pt", "Portuguese", "&#127477;&#127479;", "Avaliações de todos os tipos de produtos",
			"Link para a nossa Página Inicial", "Início", "Autor", "Autores", "Fale Conosco", "Avaliação", "Avaliações",
			"INCQ Todos os direitos reservados", "Como um Associado da Amazon, ganhamos com compras qualificadas.",
			"Login/Registrar", "Bem-vindo", "Categoria:", "Os mais populares:", "Aqui estão alguns dos nossos autores",
			"Introdução", "Conclusão", "Verificar na Amazon!",
			"Gostaríamos muito de ouvir suas opiniões. Por favor, faça login para que possamos receber sua mensagem.",
			"Assunto", "Seu comentário ou pergunta", "Compartilhe seus pensamentos",
			"Agradecemos o seu interesse. Verifique o nosso ", "Política de Privacidade",
			"Aqui estão nossos autores mais reconhecidos", "Mais..."),
	RUSSIAN("ru", "Russian", "&#127479;&#127482;", "Отзывы о различных продуктах", "Ссылка на нашу Главную Страницу",
			"Главная", "Автор", "Авторы", "Свяжитесь с Нами", "Обзор", "Обзоры", "INCQ Все права защищены",
			"Как ассоциированный участник Amazon, мы зарабатываем на квалифицированных покупках.", "Вход/Регистрация",
			"Добро пожаловать", "Категория:", "Самые популярные:", "Вот некоторые из наших авторов", "Введение",
			"Заключение", "Проверьте Amazon!",
			"Мы бы хотели услышать ваши мысли. Пожалуйста, войдите, чтобы мы могли получить ваше сообщение.", "Тема",
			"Ваш комментарий или вопрос", "Поделитесь своими мыслями", "Мы ценим ваш интерес. Проверьте нашу ",
			"Политика Конфиденциальности", "Вот наши наиболее признанные авторы", "Больше..."),
	SPANISH("es", "Spanish", "&#127466;&#127480;", " Reseñas de todo tipo de productos",
			"Enlace a nuestra Página Principal", "Inicio", "Autor", "Autores", "Contáctenos", "Reseña", "Reseñas",
			"INCQ Todos los derechos reservados", "Como Asociado de Amazon, ganamos con las compras calificadas.",
			"Iniciar Sesión/Registrarse", "Bienvenido", "Categoría:", "Los más populares:",
			"Aquí algunos de nuestros Autores", "Introducción", "Conclusión", "¡Ver en Amazon!",
			"Nos encantaría conocer tu opinión. Por favor, inicia sesión para que podamos recibir tu mensaje.",
			"Asunto", "Tu comentario o pregunta", "Comparte tus pensamientos",
			"Agradecemos tu interés. Consulta nuestra ", "Política de Privacidad",
			"Aquí están nuestros autores más reconocidos", "Más..."),
	SPANISH_MEXICO("es_MX", "Spanish (Mexico)", "&#127470;&#127481;", " Reseñas de todo tipo de productos",
			"Enlace a nuestra Página Principal", "Inicio", "Autor", "Autores", "Contáctanos", "Reseña", "Reseñas",
			"INCQ Todos los derechos reservados", "Como Asociado de Amazon, ganamos con las compras calificadas.",
			"Iniciar Sesión/Registrarse", "Bienvenido", "Categoría:", "Los más populares:",
			"Aquí algunos de nuestros Autores", "Introducción", "Conclusión", "¡Ver en Amazon!",
			"Nos encantaría conocer tu opinión. Por favor, inicia sesión para que podamos recibir tu mensaje.",
			"Asunto", "Tu comentario o pregunta", "Comparte tus pensamientos",
			"Agradecemos tu interés. Consulta nuestra ", "Política de Privacidad",
			"Aquí están nuestros autores más reconocidos", "Más..."),
	SPANISH_CO("es_CO", "Spanish (Colombia)", "&#127470;&#127481;", " Reseñas de toda clase de productos",
			"Enlace a nuestra Página Principal", "Inicio", "Autor", "Autores", "Contáctenos", "Reseña", "Reseñas",
			"INCQ Todos los derechos reservados", "Como Asociado de Amazon, ganamos de compras calificadas.",
			"Iniciar Sesión/Registrarse", "Bienvenido", "Categoría:", "Los más populares:",
			"Aquí algunos de nuestros Autores", "Introducción", "Conclusión", "¡Ver en Amazon!",
			"Nos encantaría conocer tus pensamientos. Por favor, inicia sesión para que podamos recibir tu mensaje.",
			"Asunto", "Tu comentario o pregunta", "Comparte tus pensamientos", "Apreciamos tu interés. Revisa nuestra ",
			"Política de Privacidad", "Aquí están nuestros autores más reconocidos", "Más..."),
	SPANISH_ARGENTINA("es_AR", "Spanish (Argentina)", "&#127465;&#127487;", " Reseñas de todo tipo de productos",
			"Enlace a nuestra Página Principal", "Inicio", "Autor", "Autores", "Contáctenos", "Reseña", "Reseñas",
			"INCQ Todos los derechos reservados", "Como Asociado de Amazon, ganamos de compras calificadas.",
			"Iniciar Sesión/Registrarse", "Bienvenido", "Categoría:", "Los más populares:",
			"Aquí algunos de nuestros Autores", "Introducción", "Conclusión", "¡Ver en Amazon!",
			"Nos encantaría conocer tus pensamientos. Por favor, inicia sesión para que podamos recibir tu mensaje.",
			"Asunto", "Tu comentario o pregunta", "Comparte tus pensamientos", "Apreciamos tu interés. Revisa nuestra ",
			"Política de Privacidad", "Aquí están nuestros autores más reconocidos", "Más..."),
	SPANISH_VENEZUELA("es_VE", "Spanish (Venezuela)", "&#127474;&#127486;", " Reseñas de todo tipo de productos",
			"Enlace a nuestra Página Principal", "Inicio", "Autor", "Autores", "Contáctanos", "Reseña", "Reseñas",
			"INCQ Todos los derechos reservados", "Como Asociado de Amazon, ganamos con compras calificadas.",
			"Iniciar Sesión/Registrarse", "Bienvenido", "Categoría:", "Los más populares:",
			"Aquí algunos de nuestros Autores", "Introducción", "Conclusión", "¡Ver en Amazon!",
			"Nos encantaría conocer tus opiniones. Por favor, inicia sesión para que podamos recibir tu mensaje.",
			"Asunto", "Tu comentario o pregunta", "Comparte tus pensamientos",
			"Agradecemos tu interés. Revisa nuestra ", "Política de Privacidad",
			"Aquí están nuestros autores más reconocidos", "Más..."),
	SPANISH_PERU("es_PE", "Spanish (Peru)", "&#127477;&#127466;", " Reseñas de todo tipo de productos",
			"Enlace a nuestra Página Principal", "Inicio", "Autor", "Autores", "Contáctanos", "Reseña", "Reseñas",
			"INCQ Todos los derechos reservados", "Como Asociado de Amazon, ganamos con compras calificadas.",
			"Iniciar Sesión/Registrarse", "Bienvenido", "Categoría:", "Los más populares:",
			"Aquí algunos de nuestros Autores", "Introducción", "Conclusión", "¡Ver en Amazon!",
			"Nos encantaría conocer tus opiniones. Por favor, inicia sesión para que podamos recibir tu mensaje.",
			"Asunto", "Tu comentario o pregunta", "Comparte tus pensamientos",
			"Agradecemos tu interés. Revisa nuestra ", "Política de Privacidad",
			"Aquí están nuestros autores más reconocidos", "Más..."),
	SWEDISH("sv", "Swedish", "&#127480;&#127465;", " Recensioner av alla typer av produkter", "Länk till vår Startsida",
			"Hem", "Författare", "Författarna", "Kontakta Oss", "Recension", "Recensioner",
			"INCQ Alla rättigheter reserverade", "Som Amazon-partner tjänar vi på kvalificerade köp.",
			"Logga in/Registrera dig", "Välkommen", "Kategori:", "Våra mest populära:",
			"Här är några av våra Författare", "Introduktion", "Slutsats", "Kolla på Amazon!",
			"Vi skulle älska att höra dina tankar. Vänligen logga in så att vi kan ta emot ditt meddelande.", "Ämne",
			"Din kommentar eller fråga", "Dela dina tankar", "Vi uppskattar ditt intresse. Kolla vår ",
			"Sekretesspolicy", "Här är våra mest erkända författare", "Mer..."),
	TURKISH("tr", "Turkish", "&#127481;&#127479;", " Her türlü ürün hakkında yorumlar", "Ana Sayfamıza Bağlantı",
			"Ana Sayfa", "Yazar", "Yazarlar", "Bize Ulaşın", "İnceleme", "İncelemeler", "INCQ Tüm hakları saklıdır",
			"Amazon Associate olarak uygun alışverişlerden kazanıyoruz.", "Giriş/Kayıt", "Hoş Geldiniz", "Kategori:",
			"En popülerlerimiz:", "İşte bazı yazarlarımız", "Giriş", "Sonuç", "Amazon'u Kontrol Et!",
			"Düşüncelerinizi duymayı çok isteriz. Mesajınızı alabilmemiz için lütfen giriş yapın.", "Konu",
			"Yorumunuz veya sorunuz", "Düşüncelerinizi paylaşın", "İlginiz için teşekkür ederiz. Kontrol edin ",
			"Gizlilik Politikası", "İşte en tanınmış yazarlarımız", "Daha fazla..."),
	HEBREW("he", "Hebrew", "&#127473;&#127482;", " חוות דעת על כל סוגי המוצרים", "קישור לדף הבית שלנו", "בית", "מחבר",
			"מחברים", "צור קשר", "סקירה", "סקירות", "INCQ כל הזכויות שמורות",
			"כשותפים של אמזון אנו מרוויחים מרכישות מתאימות.", "התחבר/הירשם", "ברוכים הבאים", "קטגוריה:",
			"הפופולריים ביותר שלנו:", "הנה חלק מהמחברים שלנו", "הקדמה", "סיכום", "בדוק באמזון!",
			"נשמח לשמוע את דעתכם. אנא התחברו כדי שנוכל לקבל את הודעתכם.", "נושא", "התגובה או השאלה שלך", "שתף את דעתך",
			"אנו מעריכים את עניינך. בדוק את ", "מדיניות הפרטיות", "הנה המחברים המכובדים ביותר שלנו", "עוד..."),
	SINGLISH_MALAY("sg", "Singaporean English (Singlish)/Malay", "&#127464;&#127470;",
			" Reviews lor on all kinds of products lah", "Link to our Home Page lor", "Home", "Pengarang",
			"Pengarang-pengarang", "Contact Kami", "Review", "Reviews", "INCQ Semua hak cipta terpelihara",
			"As an Amazon Associate kita earn from qualifying purchases lah.", "Login/Register lah", "Welcome lor",
			"Category:", "Our most shiok one:", "Here some of our Pengarang", "Pendahuluan", "Kesimpulan",
			"Check Amazon lah!",
			"We'd love to hear your thoughts lor. Kindly login so we can receive your message lah.", "Subjek",
			"Your komen or soalan", "Share your thoughts lah", "We appreciate your minat. Check our ", "Dasar Privasi",
			"Here are our most recognized pengarang", "More lah...");

	private static final Map<String, Language> BY_CODE = new HashMap<>();
	private static final Map<String, Language> BY_NAME = new HashMap<>();
	static {
		for (Language lang : values()) {
			BY_CODE.put(lang.code.toLowerCase(), lang);
			BY_NAME.put(lang.name, lang);
		}
	}

	public final String code;
	public final String name;
	public final String flagUnicode;
	public final String incqDesc;
	public final String home;
	public final String linkHome;
	public final String author;
	public final String authors;
	public final String contactUs;
	public final String review;
	public final String reviews;
	public final String arr;
	public final String amazon;
	public final String login;
	public final String welcome;
	public final String category;
	public final String popular;
	public final String someAuthors;
	public final String introduction;
	public final String conclusion;
	public final String checkAmazon;
	public final String contactLogin;
	public final String subject;
	public final String message;
	public final String emailSubmit;
	public final String privacyPolicyIntro;
	public final String privacyPolicy;
	public final String authDesc;
	public final String more;

	Language(String code, String name, String flagUnicode) {
		this(code, name, flagUnicode, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
				"", "", "", "", "", "");
	}

	Language(String code, String name, String flagUnicode, String incqDesc, String linkHome, String home, String author,
			String authors, String contactUs, String review, String reviews, String arr, String amazon, String login,
			String welcome, String category, String popular, String someAuthors, String introduction, String conclusion,
			String checkAmazon, String contactLogin, String subject, String message, String emailSubmit,
			String privacyPolicyIntro, String privacyPolicy, String authDesc, String more) {
		this.code = code;
		this.name = name;
		this.flagUnicode = flagUnicode;
		this.incqDesc = incqDesc;
		this.linkHome = linkHome;
		this.home = home;
		this.author = author;
		this.authors = authors;
		this.contactUs = contactUs;
		this.review = review;
		this.reviews = reviews;
		this.arr = arr;
		this.amazon = amazon;
		this.login = login;
		this.welcome = welcome;
		this.category = category;
		this.popular = popular;
		this.someAuthors = someAuthors;
		this.introduction = introduction;
		this.conclusion = conclusion;
		this.checkAmazon = checkAmazon;
		this.contactLogin = contactLogin;
		this.subject = subject;
		this.message = message;
		this.emailSubmit = emailSubmit;
		this.privacyPolicyIntro = privacyPolicyIntro;
		this.privacyPolicy = privacyPolicy;
		this.authDesc = authDesc;
		this.more = more;
	}

	public static Language findByCode(String code) {
		Language lang = BY_CODE.getOrDefault(code.toLowerCase(), ENGLISH);
		if (ENGLISH.equals(lang) && code.startsWith("es-")) {
			lang = Language.SPANISH_MEXICO;
		}
		return lang;
	}

	public static Language findByName(String name) {
		Language lang = BY_NAME.getOrDefault(name, ENGLISH);
		if (ENGLISH.equals(lang) && name.startsWith("SPANISH")) {
			lang = Language.SPANISH_MEXICO;
		}
		return lang;
	}

	public String getFlagUnicode() {
		return flagUnicode;
	}
}