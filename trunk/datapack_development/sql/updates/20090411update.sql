alter table custom_etcitem add `time` int(4) NOT NULL default '-1'  after `duration`, MODIFY `duration` int(3) NOT NULL default '0';
alter table custom_armor add `time` int(4) NOT NULL default '-1'  after `duration`, MODIFY `duration` int(3) NOT NULL default '0';
alter table custom_weapon add `time` int(4) NOT NULL default '-1'  after `duration`, MODIFY `duration` int(3) NOT NULL default '0';