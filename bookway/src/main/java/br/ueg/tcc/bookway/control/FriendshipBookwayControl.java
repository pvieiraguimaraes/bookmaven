package br.ueg.tcc.bookway.control;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.FriendshipController;

@Service
@Scope("prototype")
public class FriendshipBookwayControl extends FriendshipController {

}
