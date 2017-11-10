$(function(){
	$('.kompas-widget').each(function() {
	var endpoint = "http://apis.kompas.com/widget/v1";
	var qs = "";
	var type = "";
	var origin_style = this.getAttribute("style");
	this.setAttribute("style", "position:absolute; visibility: hidden;");

	for (i = 0; i < this.attributes.length; i++) {
	    var data = this.attributes[i].name.split("-");
	    var value = this.attributes[i].value;
	    if (data[0] == "data" && data.length > 1) {
	        data.shift();
	        data = data.join("-");
	        if (data == "type") {
	            type = value.replace(".", "/");
	        } else {
	            qs += "&" + data + "=" + value;
	        }
	    }
	}
	var url = endpoint + "/" + type + "?" + qs;
	var frame = document.createElement("iframe");
	frame.setAttribute("src", url);
	frame.setAttribute("style", "height: 100%;width: 100%;");
	frame.setAttribute("scrolling", "no");
	frame.setAttribute("frameborder", "0");
	frame.setAttribute("onload","this.parentNode.setAttribute('style','"+origin_style+"')");
	this.appendChild(frame);   
	});
});

// (function(d, s, id) {
//   var js, fjs = d.getElementsByTagName(s)[0];
//   if (d.getElementById(id)) return;
//   js = d.createElement(s); js.id = id;
//   js.src = "http://my.kompas.com/beacon/plugin/sdk/all.js";
//   fjs.parentNode.insertBefore(js, fjs);
// }(document, 'script', 'mykompas-sdk'));

// var newRule = ".fb-override *{pointer-events: none;visibility:none;}";
// var facebook_button = [
//     '.fb-share-button',
//     '#fb-share',
//     'a[rel="article_share_fb"]',
//     '.pluginShareButtonLink'
// ];


// try {
//     (function($) {
//         $(function() {
//             $("style").append(newRule);
//             for (var i = 0; i < facebook_button.length; i++) {
//                 console.log("adding class facebook-override");
//                 $(facebook_button[i]).each(function() {
//                     console.log("   on: " + facebook_button[i]);
//                     var div = $('<div></div>');
//                     var div_layer = $('<div style="position:absolute;width:100px;height:100px;"></div>');
//                     div.addClass("fb-override");
//                     console.log("tinggi:" + $(this).height());
//                     console.log("lebar:" + $(this).width());
//                     div_layer.height($(this).height());
//                     div_layer.width($(this).width());
//                     div.appendTo($(this).parent());
//                     div_layer.appendTo(div);
//                     $(this).appendTo(div);
//                 });
//             }



//             if ($().on) {
//                 console.log("on exsist");
//                 $(".fb-override").on("click", function() {
//                     trigger_facebook_share(this);
//                 });
//             } else if ($().live) {
//                 console.log("live exsist");
//                 $(".fb-override").live("click", function() {
//                     trigger_facebook_share(this);
//                 });
//             } else {
//                 console.log("direct trigger");
//                 $(".fb-override").click(function() {
//                     trigger_facebook_share(this);
//                 });
//             }

//             var twitter_bind = function(event) {
//                 var url = event.target.baseURI;
//                 var title = document.title;
//                 shareontwitter(event, url, title);
//             };

// //    twttr.events.bind('tweet', function(event) {
// //        twitter_bind(event);
// //    });
//             try {
//                 twttr.ready(function(twttr) {
//                     // Now bind our custom intent events
//                     //      twttr.events.bind('click', twitter_bind);
//                     twttr.events.bind('tweet', twitter_bind);
//                     //      twttr.events.bind('retweet', twitter_bind);
//                     //      twttr.events.bind('favorite', twitter_bind);
//                     //      twttr.events.bind('follow', twitter_bind);
//                 });
//             } catch (e) {
//                 console.log("no twttr found");
//             }
//             var trigger_facebook_share = function(obj) {

//                 var url = document.URL;
//                 var title = document.title;
//                 FB.ui(
//                         {
//                             method: 'share',
//                             href: url,
//                             display: "popup",
//                         },
//                         function(response) {
//                             if (response && !response.error_code) {
//                                 shareonfacebook(response, url, title);
//                             }
//                         }
//                 );
//             }

//             var facebook_like = function(url, html_element) {
//                 shareonfacebook(null, document.URL, document.title);
//             }

//             var facebook_unlike = function(url, html_element) {
//             }

//             var shareonfacebook = function(options, url, title) {
//                 var x = options;
//                 var req = $.ajax({
//                     url: "http://api.my.kompas.com/panel/shareonfacebook/?jsoncallback=?",
//                     data: {
//                         app_id: "kompascom",
//                         app_key: "k01sbbdjweri938X",
//                         url: url,
//                         title: title,
//                     },
//                     type: "GET",
//                     dataType: "jsonp"
//                 });
//                 req.done(options.callback)
//             };

//             var shareontwitter = function(options, url, title) {
//                 var x = options;
//                 var req = $.ajax({
//                     url: "http://api.my.kompas.com/panel/shareontwitter/?jsoncallback=?",
//                     data: {
//                         app_id: "kompascom",
//                         app_key: "k01sbbdjweri938X",
//                         url: url,
//                         title: title,
//                     },
//                     type: "GET",
//                     dataType: "jsonp"
//                 });

//                 req.done(options.callback)
//             };
//             try {
//                 FB.Event.subscribe('edge.create', facebook_like);
//                 FB.Event.subscribe('edge.remove', facebook_unlike);
//             }catch(e){console.log(e)}

//         });
//     })(jQuery);
// } catch (e) {
//     console.log(e);
// }

