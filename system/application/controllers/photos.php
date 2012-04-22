<?php

class Photos extends Controller {

	function Photos()
	{
    parent::Controller();	
    $this->load->model('photo_model');
    $this->load->helper('text');
    $this->load->helper('typography');
	}
	
	function index()
  {
    $city = $this->input->post('city');
    if($city)
    {
      $this->session->set_userdata('city', $city);
    }

    $data = array();

    $data['photos'] = $this->photo_model->get_photos(array(
      'completed' => FALSE,
      'sort' => 'most_vote',
      'lokasi' => $this->user_model->get_city()
    ));

    $data['photos_resolved'] = $this->photo_model->get_photos(array(
      'completed' => TRUE,
      'sort' => 'most_vote',
      'lokasi' => $this->user_model->get_city()
    ));

		$this->load->view('htmlhead');
		$this->load->view('nav-bar');
		$this->load->view('photos', $data);
		$this->load->view('footer');
  }

  function view($pid=FALSE)
  {
    $data = array();

    $data['photos'] = $this->photo_model->get_photos(array(
      'id' => $pid
    ));

		$this->load->view('htmlhead');
		$this->load->view('nav-bar');
		$this->load->view('photos-detail', $data);
    $this->load->view('footer');
  }

  function upload($edit=FALSE)
  {
    $record = array(
      'nama'          => $this->input->post('nama'),
      'gambar'        => $this->input->post('gambar'),
      'keterangan'    => $this->input->post('keterangan'),
      'tanggal'       => date('Y-m-d'),
      'lokasi'        => '',
      'status'        => '0', // un-resolved
      'vote'          => '0', // vote 0
      'lat'           => trim($_POST['lat']), // latitude
      'long'          => trim($_POST['lon']) // longitude
    );

    $id = $this->input->post('id');
    if($id)
    {
      $record['id'] = $id;
    }

    $binFile = $_FILES['gambar']['tmp_name'];
    log_message('error', 'gambar tmp_name is: '.$binFile);
    log_message('error', print_r($_FILES, TRUE));
    /*
    if (isset($binFile) && $binFile != "none") {
      $record['gambar'] = (fread(fopen($binFile, "r"), filesize($binFile)));
    }*/
    if (isset($binFile))
    {
      $hndl = fopen($binFile,"r");
      $isize = sizeof($binFile);

      $imgdata = "";
      while(!feof($hndl)){
        $imgdata .= fread($hndl,$isize);
      };

      $record['gambar'] = $imgdata;
    }

    $get_city = TRUE;
    $location = $this->location_model->where_is($record['lat'], $record['long'], $get_city);
    $record['lokasi'] = ($location) ? $location.'' : '';

    log_message('error', 'querying with lat: '.$record['lat'].' and long: '.$record['long']);
    log_message('error', 'lokasi: '.$record['lokasi']);

    $ins = $this->db->insert('garbage', $record);

    echo 'Success!';
  }

  function edit_response($pid=FALSE)
  {
    if(!$pid || !$this->user_model->is_logged_in())
    {
      return;
    }

    $this->db->set('response', $this->input->post('response-edit'));
    $this->db->set('status'  , $this->input->post('response-resolve'));
    $this->db->where('id', $pid);
    $this->db->limit(1);
    $upd = $this->db->update('garbage');

    redirect('photos/view/'.$pid);
  }

  function request_action($pid=FALSE)
  {
    if(!$pid)
    {
      return;
    }

    $this->photo_model->vote_for($pid);
  }
}

/* End of file photos.php */
/* Location: ./system/application/controllers/photos.php */
