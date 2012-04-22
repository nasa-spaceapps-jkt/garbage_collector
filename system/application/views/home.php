
<div id="choose-city" class="hero-unit">
  <div class="title">
    <h2>What City are You In?</h2>
    <p>Berada di kota mana kamu sekarang?</p>
  </div>

  <div class="row">
    <div class="span8">
      <form method="post" action="<?=site_url('photos')?>" id="form-choose-city" class="" style="">
        <p><input id="my-city" name="city" type="text" class="input-xlarge" /><span id="guess-right" style="display:none;opacity: 0.6;font-size: smaller;"> (did I guess that right?)</span></p>
        <p><input type="submit" class="btn btn-large btn-primary" value="See Environmental Degradation &raquo;" /></p>
        <!--<p><a href="#add-photos" class="goto-step btn btn-large btn-primary">Go</a></p>-->
      </form>
    </div>
  </div>
</div>

<script type="text/javascript">
$(document).ready(function(){
  if (typeof(console.log) === 'undefined') {
    console = {};
    console.log = function (){};
  }

  function whereIs (lat, long) {
    var endPoint = '<?=site_url('util/where_is')?>' + '/' + lat + '/' + long;
    console.log(endPoint);
    $.ajax({
      type:'get',
      url: endPoint,
      success: function (res) {
        var city, resJSON = xml2json.parser(res);
        if (resJSON && resJSON.resultset.error == 0) {
          city = resJSON.resultset.result.state;
          $('input#my-city').val(city);
          $('#guess-right').fadeIn();
        }
      },
      error: function () {
        console.log('error looking up location');
        console.log(endPoint);
      }
    });
    return true;
  }

  function success (position) {
    console.log(position);
    whereIs(position.coords.latitude, position.coords.longitude);
  }
  function error (msg) {
    var s = document.querySelector('#status');
    s.innerHTML = typeof msg == 'string' ? msg : "failed";
    s.className = 'fail';
  }

  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(success, error);
  } else {
    error('not supported');
  }
});
</script>
