"""Deploy Karma"""

import os
print os.environ['PATH']

from fabric.api import env,run,put,sudo
from fabric.tasks import execute;

import getpass
import json
from cStringIO import StringIO;
env.user=None
env.host_string=None
env.key_filename=None


class Deployer:
    context={}
    def __init__(self,context):
        modified=False
        with open("deploy.json","rw") as conf_fd:
            env_conf=json.load(conf_fd)
            if env_conf['keyfile']=='':
                env_conf['keyfile']=raw_input("keyfile is NOT initialized, please enter the key file path\r\n")
                modified=True                
            if modified:
                with open("deploy.json","w") as conf_fd:
                    json.dump(env_conf,conf_fd)
            env.user=env_conf['user']
            env.host_string=env_conf['host']
            env.key_filename=env_conf['keyfile']
            
        for key in context:
            self.context[key]=context[key]

    def set_apt_repo(self):
        for aptRepo in self.context['apt_repos']:
            sudo("sudo add-apt-repository -y %s" % aptRepo)
            sudo("apt-get update")

    def install_apt(self):
        for apt in self.context['apts']:
            sudo("apt-get -y --fix-missing install %s " % apt)

    def install_tar(self):
        for tar in self.context['tars']:
            print("install TAR %s " % tar['name'])
            sudo("mkdir -p %s" % tar['target'])
            fileName=tar['download'][tar['download'].rfind("/")+1:];
            sudo("cd %s;wget %s;tar zxvf %s" %(tar['target'],tar['download'],fileName))

    def stop_service(self):
        try:
            run("service %s stop && sleep 30" % self.context['tomcat_service_name'])
        except:
            pass

    def start_service(self):
        run("service %s start && sleep 1 " % self.context['tomcat_service_name'])
    
    def clean_war(self):
        for remotePath in context['remove_files']:
            sudo("rm -rf %s"% remotePath)

    def deploy_war(self):
        for remotePath in context['files']:
            with open(context['files'][remotePath]) as fd:
                put(fd,remotePath,mode=777, use_sudo=True)

context={
    'apt_repos':['ppa:webupd8team/java'],
    'apts':["openjdk-7-jdk"],
    'tars':[
        {'name':'tomcat7','target':'~/tools','download':'http://ftp.wayne.edu/apache/tomcat/tomcat-7/v7.0.59/bin/apache-tomcat-7.0.59.tar.gz'}
    ],
    'tomcat_service_name':'tomcat',
    'files':{
        '/home/ubuntu/tomcat7/webapps/ThankWeb.war':'target/ThankWeb.war'
    },
    'remove_files':[
        "/home/ubuntu/tomcat7/webapps/ThankWeb.war",
        "/home/ubuntu/tomcat7/webapps/ThankWeb"
    ]
}

deployer = Deployer(context)
deployer.stop_service()
deployer.clean_war()
deployer.deploy_war()
deployer.start_service()

#deployer.set_apt_repo();
#deployer.install_apt();
#deployer.installTar()
