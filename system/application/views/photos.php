<div id="photos" class="">

<div class="row">
  <div class="span12">
    <h2>Garbage Piling in <?=htmlspecialchars($this->user_model->get_city());?></h2>

    <div class="row">
      <div class="span12">
        <ul class="thumbnails">
          <?php for($i = 0; $i < 20 && FALSE; $i++): ?>
            <li class="span3">
              <div class="thumbnail">
                <img src="<?=base_url();?>assets/images/360x260.gif" />
                <div class="caption">
                  <!--<h3>Unwanted Garbage Pile</h3>-->
                  <p>
                    Lots of garbage piling up since last month. Please do something!
                  </p>
                  <p><span class="icon-user"></span> 30 </p>
                  <p><a href="<?=site_url('photos/view/1234')?>" class="goto-step btn btn-large">View</a></p>
                </div>
              </div>
            </li>
          <?php endfor; ?>
          <?php if($photos): foreach($photos as $photo): ?>
            <li class="span3">
              <div class="thumbnail" >
                <?php /* <img src="<?php echo 'data:image/jpeg;base64,'.$photo->gambar; ?>" alt="" /> */ ?>
                <img  style="width: 260px; height: 180px; overflow:hidden;" src="<?=site_url('util/img/'.$photo->id); ?>" alt="" />
                <div class="caption">
                  <!--<h3>Unwanted Garbage Pile</h3>-->
                  <p>
                    <?php echo word_limiter(htmlspecialchars(strip_tags($photo->keterangan)), 10); ?>
                  </p>
                    <p><span class="icon-user"></span> <?php echo $photo->vote; ?></p>
                  <p>
                    <a href="<?=site_url('photos/view/'.$photo->id)?>" class="goto-step btn btn-large">View</a>
                    <?php if($photo->status == 1): ?>
                    <span style="color: green;" class="icon-ok"></span> <span style="color:green;">Resolved!</span>
                    <?php endif; ?>
                  </p>
                </div>
              </div>
            </li>
          <?php endforeach; endif; ?>

          <?php if($photos_resolved): foreach($photos_resolved as $photo): ?>
            <li class="span3">
              <div class="thumbnail" >
                <img  style="width: 260px; height: 180px; overflow:hidden;" src="<?=site_url('util/img/'.$photo->id); ?>" alt="" />
                <div class="caption">
                  <p>
                    <?php echo word_limiter(htmlspecialchars(strip_tags($photo->keterangan)), 10); ?>
                  </p>
                    <p><span class="icon-user"></span> <?php echo $photo->vote; ?></p>
                  <p>
                    <a href="<?=site_url('photos/view/'.$photo->id)?>" class="goto-step btn btn-large">View</a>
                    <?php if($photo->status == 1): ?>
                    <span style="color: green;" class="icon-ok"></span> <span style="color:green;">Resolved!</span>
                    <?php endif; ?>
                  </p>
                </div>
              </div>
            </li>

          <?php endforeach; endif; ?>

        </ul>
      </div>
    </div>
  </div>
</div>

</div>
