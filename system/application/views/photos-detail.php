<div id="photos-detail" class="">

<div class="row">
  <div class="span12">
    <!--<h1 style="margin-bottom: 12px;" class="title"><span class="icon-camera-retro"></span> Unwanted Garbage Pile</h1>-->
    <a style="margin-bottom: 18px;" href="<?=site_url('photos')?>" class="btn btn-large">&laquo; Back</a>
    <?php if(!$photos): ?>
      <div class=""><p>You broke me :(</p></div>
    <?php else: ?>
      <?php $photo = $photos[0]; ?>
    <h3 style="margin-bottom: 18px;" class="title"><span class="icon-user"></span> <?=$photo->vote?> people have requested an action on this issue:</h3>
    <div class="row">
      <div class="span4">
        <div class="thumbnail">
        <img src="<?=site_url('util/img/'.$photo->id)?>" alt="" />
          <div class="caption">
            <?php echo auto_typography($photo->keterangan); ?>
            <p>Posted: <?=get_relative_time($photo->tanggal.' 00:00:00');?> by <strong><?=htmlspecialchars($photo->nama)?></strong></p>

            <p>
              <a target="_blank" href="https://twitter.com/share" class="twitter-share-button" data-lang="en"><span class="icon-twitter-sign"></span> Tweet</a>
              <script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0];if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src="//platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>
               | 
               <a target="_blank" href="https://facebook.com/sharer.php?u=<?=current_url()?>"><span class="icon-facebook-sign"></span> Share</a>
            </p>
          </div>
        </div>
      </div>
      <div class="span8">
        <div class="well">

          <h2>Status</h2>
          <?php if($photo->status == 1): ?>
          <p style="color: green; font-size: larger;"><span class="icon-ok"></span> Resolved</p>
          <?php else: ?>
          <p style="font-size: larger;"><span class="icon-eye-open"></span> Needs review </p>
          <?php endif; ?>

          <h2>Response from authority:</h2>
          <!-- response -->
          <?php if($photo->response): ?>
          <?=auto_typography(htmlspecialchars($photo->response)); ?>
          <?php else: ?>
          <p>None yet.</p>
          <?php endif; ?>
          <br>

          <?php if($this->user_model->is_logged_in()): ?>
          <!-- form if logged in as authority -->
          <?php $resolve_opposite = $photo->status == 1 ? ' checked="checked" ' : ''; ?>
          <p>Editing a response as: <strong>Clean Jakarta Society (LSM Jakarta Bersih)</strong></p>
          <form method="post" action="<?=site_url('photos/edit_response/'.$photo->id)?>">
            <textarea name="response-edit" class="input-xlarge"><?=htmlspecialchars($photo->response)?></textarea>
            <input type="submit" class="btn btn-large btn-primary" value="Save" />
            <br>
            <label style="display: inline; " for="response-resolve">Resolved: </label>
            <input style="display: inline; " <?=$resolve_opposite?> type="checkbox" name="response-resolve" id="response-resolve" value="1" />
          </form>
          <?php endif; ?>
        </div>
        <div id="wmcc" class="well">
          <?php if($photo->lat !== NULL && $photo->long !== NULL): ?>
          <div id="mapCanvasContainer" lat="<?=$photo->lat?>" long="<?=$photo->long?>">
            
          </div>
          <?php endif; ?>
        </div>
        <?php if($this->user_model->is_logged_in()): ?>
        <!-- upload form -->
        <!--
        <div class="well">
          <form method="post" action="<?=site_url('photos/upload')?>" enctype="multipart/form-data">
            <input type="hidden" name="MAX_FILE_SIZE" value="2200000" />
            <input type="hidden" name="id" value="1235" />
            <input type="hidden" name="lat" value="-6.217374962790827" />
            <input type="hidden" name="lon" value="106.8151330947876" />
            <input type="hidden" name="nama" value="<?=htmlspecialchars($this->user_model->get_name());?>" />
            <input type="hidden" name="keterangan" value="test" />
            <label for="my-gambar">Image (max 2MB): </label><input id="my-gambar" type="file" name="gambar" />
            <input type="submit" name="submit" class="btn btn-primary " value="Upload" />
          </form>
        </div>
        <div class="well">
          <form method="post" action="<?=site_url('photos/upload/edit')?>" enctype="multipart/form-data">
            <input type="hidden" name="MAX_FILE_SIZE" value="2200000" />
            <input type="hidden" name="id" value="<?=$photo->id?>" />
            <input type="hidden" name="lat" value="<?=$photo->lat?>" />
            <input type="hidden" name="lon" value="<?=$photo->long?>" />
            <input type="hidden" name="nama" value="<?=htmlspecialchars($photo->nama)?>" />
            <input type="hidden" name="keterangan" value="<?=htmlspecialchars($photo->keterangan)?>" />
            <label for="my-gambar">Image showing resolved issue (max 2MB): </label><input id="my-gambar" type="file" name="gambar" />
            <input type="submit" name="submit" class="btn btn-primary " value="Upload" />
          </form>
        </div>
        -->
        <?php endif; ?>
        <div class="well">
          <p>If this is troubling you, click here:</p>
          <?php if($this->photo_model->has_voted_for($photo->id)): ?>
          <p><a disabled="disabled" class="vote disabled btn btn-large" href="#">Voted</a></p>
          <?php else: ?>
          <p><a class="vote btn btn-large btn-primary" href="<?=site_url('photos/request_action/'.$photo->id)?>">Request an Action</a></p>
          <?php endif; ?>
        </div>
      </div>
    </div>
    <?php endif; // end if photo ?>
  </div>
</div>

</div>

<script type="text/javascript">
$(document).ready(function(){

  (function makeGMap() {
    var s = $('#mapCanvasContainer');

    if (s.length === 0) {
      $('#wmcc').hide();
    }
    if (s.hasClass('success')) {
      // not sure why we're hitting this twice in FF, I think it's to do with a cached result coming back    
      return;
    }
    
    s.addClass('success');
    
    var mapcanvas = $('<div>');
    mapcanvas.attr('id', 'mapcanvas');
    mapcanvas.css('height', '300px');
    mapcanvas.css('width', '400px');

    s.append(mapcanvas);
    
    var latlng = new google.maps.LatLng(s.attr('lat'), s.attr('long'));
    var myOptions = {
      zoom: 15,
      center: latlng,
      mapTypeControl: false,
      navigationControlOptions: {style: google.maps.NavigationControlStyle.SMALL},
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    var map = new google.maps.Map(document.getElementById("mapcanvas"), myOptions);
    
    var marker = new google.maps.Marker({
        position: latlng, 
        map: map, 
        title:"You are here! "
    });
  }());

  $('a.vote').click(function(e){
    var that = this;
    $(this).attr('disabled', 'disabled').addClass('disabled');

    $.ajax({
      url: $(that).attr('href'),
      type: 'post',
      success: function () {
        $(that).removeClass('btn-primary').text('Voted');
      },
      error: function () {

      }
    });

    e.stopPropagation();
    e.preventDefault();
  });

});
</script>
