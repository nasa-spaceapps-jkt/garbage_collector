<?php

class Photo_model extends Model {

	function Photo_model()
	{
		parent::Model();	
  }

  function get_photos($opt = array())
  {
    $this->db->select('id');
    $this->db->select('nama');
    $this->db->select('gambar');
    $this->db->select('keterangan');
    $this->db->select('tanggal');
    $this->db->select('lokasi');
    $this->db->select('status');
    $this->db->select('vote');
    $this->db->select('lat');
    $this->db->select('long');
    $this->db->select('response');
    $this->db->select('gambar_resolve');

    if(isset($opt['id']))
    {
      $this->db->where('id', $opt['id']);
      $this->db->limit(1);
    }

    if(isset($opt['lokasi']))
    {
      $this->db->like('lokasi', $opt['lokasi']);
    }

    if(isset($opt['completed']))
    {
      if($opt['completed'] === TRUE)
      {
        $this->db->where('status', 1);
      }
      else
      {
        $this->db->where('status', 0);
      }
    }

    if(isset($opt['sort']))
    {
      if($opt['sort'] == 'most_vote')
      {
        $this->db->order_by('vote', 'desc');
      }
    }

    $this->db->from('garbage');
    $rs = $this->db->get();

    if($rs->num_rows() === 0)
    {
      return FALSE;
    }
    else
    {
      return $rs->result();
    }
  }

  function vote_for($pid=FALSE)
  {
    if(!$pid) return;

    $this->db->set('vote', 'vote + 1', FALSE);
    $this->db->where('id', $pid);
    $this->db->limit(1);
    $upd = $this->db->update('garbage');

    if($upd)
    {
      $this->session->set_userdata('voted-for-'.$pid, 1);
    }

    return TRUE;
  }

  function has_voted_for($pid='')
  {
    if($this->session->userdata('voted-for-'.$pid))
    {
      return TRUE;
    }
    else
    {
      return FALSE;
    }
  }
}

/* End of file photo_model.php */
/* Location: ./system/application/models/photo_model.php */
