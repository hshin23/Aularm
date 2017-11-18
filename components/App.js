/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  Button,
  Platform,
  StyleSheet,
  Text,
  View
} from 'react-native';

import { StackNavigator, } from 'react-navigation';

class Questionnaire extends React.Component {
  render() {
    return(
      <Text>Ye</Text>
    );
  }
}

export default class App extends Component<{}> {
  //This is for determining what path we go to
 
  render() {
    return (
      <View style={{flex: 1, padding: 60, backgroundColor: 'grey'}}>
	<Text style={styles.welcome}>Welcome to A4DN</Text>
        <Text style={{textAlign:'center'}}>The alarm system based around you!</Text>
	<Button 
          onPress={() => this.props.navigation.navigate("Quest",{screen: "Questionnaire"})} 
          title="Get Started"/>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  welcome: {
    fontSize: 40,
    textAlign: 'center',
    margin: 20,
  }
}); 

const Navi = StackNavigator({
  Home: { screen: App },
  Quest: { screen: Questionnaire },
});
